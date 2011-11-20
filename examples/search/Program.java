/*
 * Copyright 2011 Splunk, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

// UNDONE: Add additional search args
// UNDONE: Add additional output args (offset, count, field_list, f)

import com.splunk.Args;
import com.splunk.Job;
import com.splunk.http.HTTPException;
import com.splunk.sdk.Command;
import com.splunk.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;

public class Program {
    static String[] outputChoices = new String[] {
        "events", "results", "preview", "searchlog", "summary", "timeline"
    };
    static String outputDescription = 
        "Which search results to output {events, results, preview, searchlog, summary, timeline} (default: results)";

    static String outputModeDescription = 
        "Search output format {csv, raw, json, xml} (default: xml)";

    public static void main(String[] args) {
        try {
            run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    static void run(String[] args) throws IOException {
        Command command = Command.splunk("search");
        command.addRule("output", String.class, outputDescription);
        command.addRule("output_mode", String.class, outputModeDescription);
        command.addRule("verbose", "Display search progress");
        command.parse(args);

        if (command.args.length != 1)
            Command.error("Search expression required");
        String query = command.args[0];

        String output = "results";
        if (command.opts.containsKey("output")) {
            output = (String)command.opts.get("output");
            if (!Arrays.asList(outputChoices).contains(output))
                Command.error("Unsupported output: '%s'", output);
        }

        String outputMode = "xml";
        if (command.opts.containsKey("output_mode"))
            outputMode = (String)command.opts.get("output_mode");

        boolean verbose = command.opts.containsKey("verbose");

        Service service = Service.connect(command.opts);

        // Check the syntax of the query.
        try {
            Args parseArgs = new Args("parse_only", true);
            service.parse(query, parseArgs);
        }
        catch (HTTPException e) {
            String detail = e.getDetail();
            Command.error("query '%s' is invalid: %s", query, detail);
        }

        // Create a search job for the given query & query arguments.
        Args queryArgs = new Args();
        Job job = service.getJobs().create(query, queryArgs);

        // Wait until results are available.
        while (true) {
            // Determine if there are any outputs available.
            if (output.equals("preview") || output.equals("searchlog"))
                break;
            if (output.equals("events") && job.getEventIsStreaming())
                break;
            if (output.equals("results") && job.getResultIsStreaming())
                break;
            if (job.isDone()) 
                break;

            // If no outputs are available, optionally print status and wait.
            if (verbose) {
                float progress = job.getDoneProgress() * 100.0f;
                int scanned = job.getScanCount();
                int matched = job.getEventCount();
                int results = job.getResultCount();
                System.out.format(
                    "\r%03.1f%% done -- %d scanned -- %d matched -- %d results",
                    progress, scanned, matched, results);
            }

            try { Thread.sleep(2000); }
            catch (InterruptedException e) {}

            job.refresh();
        }
        if (verbose) System.out.println("");

        InputStream stream = null;

        Args outputArgs = new Args();
        outputArgs.put("output_mode", outputMode);

        if (output.equals("results"))
            stream = job.getResults(outputArgs);
        else if (output.equals("events"))
            stream = job.getEvents(outputArgs);
        else if (output.equals("preview"))
            stream = job.getResultsPreview(outputArgs);
        else if (output.equals("searchlog"))
            stream = job.getSearchLog(outputArgs);
        else if (output.equals("summary"))
            stream = job.getSummary(outputArgs);
        else if (output.equals("timeline"))
            stream = job.getTimeline(outputArgs);
        else assert(false);

        InputStreamReader reader = new InputStreamReader(stream);

        // UNDONE: Not outputting "tail" of result stream
        int size = 1024;
        char[] buffer = new char[size];
        while (true) {
            int count = reader.read(buffer);
            if (count == -1) break;
            System.out.println(buffer);
            if (count < size) break;
        }

        job.cancel();
    }
}