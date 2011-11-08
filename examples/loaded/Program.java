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

import com.splunk.*;

import java.util.Date;
import java.util.Map;

public class Program extends com.splunk.sdk.Program {
    public static void main(String[] args) {
        Program program = new Program();
        try {
            program.init(args).run();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Service connect() {
        Service service = new Service(this.host, this.port, this.scheme);
        service.login(this.username, this.password);
        return service;
    }

    public void printActions(Map<String, String> actions) {
        if (actions == null) return;
        for (Map.Entry entry : actions.entrySet()) {
            System.out.format("action %s => %s\n", 
                entry.getKey(), entry.getValue());
        }
    }

    public void printApplication(Application app) {
        printEntity(app);
        printField("CheckForUpdates", app.getCheckForUpdates());
        printField("Label", app.getLabel());
        printField("Version", app.getVersion());
        printField("Configured", app.isConfigured());
        printField("Manageable", app.isManageable());
        printField("Visible", app.isVisible());
    }

    public void printResource(Resource resource) {
        System.out.format("## %s\n", resource.getName());
        System.out.format("title = %s\n", resource.getTitle());
        System.out.format("path = %s\n", resource.getPath());
    }

    public void printEntity(Entity entity) {
        System.out.println("");
        if (entity == null) {
            System.out.println("null");
            return;
        }
        printResource(entity);
        printActions(entity.getActions());
        Map<String, Object> content = entity.getContent();
        if (content != null) {
            for (Map.Entry entry : content.entrySet()) {
                System.out.format("%s = %s\n",
                    entry.getKey(), entry.getValue().toString());
            }
        }
    }

    public <T extends Resource> void 
    printResources(ResourceCollection<T> resources) {
    	System.out.format("\n# %s\n", resources.getPath());
        System.out.format("path = %s\n", resources.getPath());
        printActions(resources.getActions());
        System.out.format("keys = %s\n", resources.keySet().toString());
        for (T resource : resources.values()) 
            printResource(resource);
    }

    public <T extends Entity> void 
    printEntities(EntityCollection<T> entities) {
    	System.out.format("\n# %s\n", entities.getPath());
        System.out.format("path = %s\n", entities.getPath());
        printActions(entities.getActions());
        System.out.format("keys = %s\n", entities.keySet().toString());
        for (T entity : entities.values()) 
            printEntity(entity);
    }

    void printField(String field, boolean value) {
        System.out.format("%s = %b\n", field, value);
    }

    void printField(String field, float value) {
        System.out.format("%s = %f\n", field, value);
    }

    void printField(String field, Date value) {
        System.out.format("%s = %s\n", 
            field, value == null ? "null" : value.toString());
    }

    void printField(String field, int value) {
        System.out.format("%s = %d\n", field, value);
    }

    void printField(String field, String value) {
        System.out.format("%s = %s\n", field, value == null ? "null" : value);
    }

    void printIndex(Index index) {
        printEntity(index);
        printField("AssureUTF8", index.getAssureUTF8());
        printField("BlockSignSize", index.getBlockSignSize());
        printField("BlockSignatureDatabase", index.getBlockSignatureDatabase());
        printField("ColdPath", index.getColdPath());
        printField("ColdPathExpanded", index.getColdPathExpanded());
        printField("ColdToFrozenDir", index.getColdToFrozenDir());
        printField("ColdToFrozenScript", index.getColdToFrozenScript());
        printField("CompressRawdata", index.getCompressRawdata());
        printField("CurrentDBSizeMB", index.getCurrentDBSizeMB());
        printField("DefaultDatabase", index.getDefaultDatabase());
        printField("EnableRealtimeSearch", index.getEnableRealtimeSearch());
        printField("FrozenTimePeriodInSecs", index.getFrozenTimePeriodInSecs());
        printField("HomePath", index.getHomePath());
        printField("HomePathExpanded", index.getHomePathExpanded());
        printField("IndexThreads", index.getIndexThreads());
        printField("LastInitTime", index.getLastInitTime());
        printField("MaxConcurrentOptimizes", index.getMaxConcurrentOptimizes());
        printField("MaxDataSize", index.getMaxDataSize());
        printField("MaxHotBuckets", index.getMaxHotBuckets());
        printField("MaxHotIdleSecs", index.getMaxHotIdleSecs());
        printField("MaxHotSpanSecs", index.getMaxHotSpanSecs());
        printField("MaxMemMB", index.getMaxMemMB());
        printField("MaxMetaEntries", index.getMaxMetaEntries());
        printField("MaxRunningProcessGroups", index.getMaxRunningProcessGroups());
        printField("MaxTime", index.getMaxTime());
        printField("MaxTotalDataSizeMB", index.getMaxTotalDataSizeMB());
        printField("MaxWarmDBCount", index.getMaxWarmDBCount());
        printField("MemPoolMB", index.getMemPoolMB());
        printField("MinRawFileSyncSecs", index.getMinRawFileSyncSecs());
        printField("MinTime", index.getMinTime());
        printField("PartialServiceMetaPeriod", index.getPartialServiceMetaPeriod());
        printField("QuarantineFutureSecs", index.getQuarantineFutureSecs());
        printField("QuarantimePastSecs", index.getQuarantinePastSecs());
        printField("RawChunkSizeBytes", index.getRawChunkSizeBytes());
        printField("RotatePeriodInSecs", index.getRotatePeriodInSecs());
        printField("ServiceMetaPeriod", index.getServiceMetaPeriod());
        printField("SuppressBannerList", index.getSuppressBannerList());
        printField("Sync", index.getSync());
        printField("SyncMeta", index.getSyncMeta());
        printField("ThawedPath", index.getThawedPath());
        printField("ThawedPathExpanded", index.getThawedPathExpanded());
        printField("ThrottleCheckPeriod", index.getThrottleCheckPeriod());
        printField("TotalEventCount", index.getTotalEventCount());
        printField("isDisabled", index.isDisabled());
        printField("isInternal", index.isInternal());
    }

    void printUser(User user) {
        printEntity(user);
        printField("DefaultApp", user.getDefaultApp());
        printField("DefaultAppIsUserOverride", user.getDefaultAppIsUserOverride());
        printField("DefaultAppSourceRole", user.getDefaultAppSourceRole());
        printField("Email", user.getEmail());
        printField("Password", user.getPassword());
        printField("RealName", user.getRealName());
        printField("Roles", user.getRoles().toString());
    }

    void printJob(Job job) {
        printEntity(job);
        printField("CursorTime", job.getCursorTime().toString());
        printField("delegate", job.getDelegate());
        printField("DiskUsage", job.getDiskUsage());
        printField("DispatchState", job.getDispatchState());
        printField("DoneProgress", job.getDoneProgress());
        printField("DropCount", job.getDropCount());
        printField("EarliestTime", job.getEarliestTime().toString());
        printField("EventAvailableCount", job.getEventAvailableCount());
        printField("EventCount", job.getEventCount());
        printField("EventFieldCount", job.getEventFieldCount());
        printField("EventIsStreaming", job.getEventIsStreaming());
        printField("EventIsTruncated", job.getEventIsTruncated());
        printField("EventSearch", job.getEventSearch());
        printField("EventSorting", job.getEventSorting());
        printField("Keywords", job.getKeywords());
        printField("Label", job.getLabel());
        printField("LatestTime", job.getLatestTime().toString());
        printField("NumPreviews", job.getNumPreviews());
        printField("Priority", job.getPriority());
        printField("RemoteSearch", job.getRemoteSearch());
        printField("ReportSearch", job.getReportSearch());
        printField("ResultCount", job.getResultCount());
        printField("ResultIsStreaming", job.getResultIsStreaming());
        printField("ResultPreviewCount", job.getResultPreviewCount());
        printField("RunDuration", job.getRunDuration());
        printField("ScanCount", job.getScanCount());
        printField("Search", job.getSearch());
        printField("SearchEarliestTime", job.getSearchEarliestTime());
        printField("SearchLatestTime", job.getSearchLatestTime());
        printField("Sid", job.getSid());
        printField("StatusBuckets", job.getStatusBuckets());
        printField("Ttl", job.getTtl());
        printField("IsDone", job.isDone());
        printField("IsFailed", job.isFailed());
        printField("IsPaused", job.isPaused());
        printField("IsPreviewEnabled", job.isPreviewEnabled());
        printField("IsRealTimeSearch", job.isRealTimeSearch());
        printField("IsRemoteTimeline", job.isRemoteTimeline());
        printField("IsSaved", job.isSaved());
        printField("IsSavedSearch", job.isSavedSearch());
        printField("IsZombie", job.isZombie());
    }

    void printMessage(Message message) {
        printEntity(message);
        printField("Key", message.getKey());
        printField("Value", message.getValue());
    }

    void printSavedSearch(SavedSearch search) {
        printEntity(search);
        printField("ActionEmailSendResults", search.getActionEmailSendResults());
        printField("ActionEmailTo", search.getActionEmailTo());
        printField("AlertExpires", search.getAlertExpires());
        printField("AlertSevertiy", search.getAlertSeverity());
        printField("AlertSuppresss", search.getAlertSuppress());
        printField("AlertSuppressPriod", search.getAlertSuppressPeriod());
        printField("AlertTrack", search.getAlertTrack());
        printField("AlertComparator", search.getAlertComparator());
        printField("AlertCondition", search.getAlertCondition());
        printField("AlertThreshold", search.getAlertThreshold());
        printField("AlertType", search.getAlertType());
        printField("CronSchedule", search.getCronSchedule());
        printField("Description", search.getDescription());
        printField("DispatchBuckets", search.getDispatchBuckets());
        printField("DispatchEarliestTime", search.getDispatchEarliestTime());
        printField("DispatchLatestTime", search.getDispatchLatestTime());
        printField("DispatchLookups", search.getDispatchLookups());
        printField("DispatchMaxCount", search.getDispatchMaxCount());
        printField("DispatchMaxTime", search.getDispatchMaxTime());
        printField("DispatchReduceFreq", search.getDispatchReduceFreq());
        printField("DispatchSpawnProcess", search.getDispatchSpawnProcess());
        printField("DispatchTimeFormat", search.getDispatchTimeFormat());
        printField("DispatchTtl", search.getDispatchTtl());
        printField("DisplayView", search.getDisplayView());
        printField("MaxConcurrent", search.getMaxConcurrent());
        printField("NextScheduledTime", search.getNextScheduledTime());
        printField("QualifiedSearch", search.getQualifiedSearch());
        printField("RealtimeSchedule", search.getRealtimeSchedule());
        printField("RequestUiDispatchApp", search.getRequestUiDispatchApp());
        printField("RequestUiDispatchView", search.getRequestUiDispatchView());
        printField("RestartOnSearchPeerAdd", search.getRestartOnSearchPeerAdd());
        printField("RunOnStartup", search.getRunOnStartup());
        printField("Search", search.getSearch());
        printField("Vsid", search.getVsid());
        printField("isActionEmail", search.isActionEmail());
        printField("isActionPopulateLookup", search.isActionPopulateLookup());
        printField("isActionRss", search.isActionRss());
        printField("isActionScript", search.isActioncScript());
        printField("isActionSummaryIndex", search.isActionSummaryIndex());
        printField("isDigestMode", search.isDigestMode());
        printField("isDisabled", search.isDisabled());
        printField("isScheduled", search.isScheduled());
        printField("isVisible", search.isVisible());
    }

    void printServiceInfo(ServiceInfo info) {
        printEntity(info);
        printField("Build", info.getBuild());
        printField("CpuArch", info.getCpuArch());
        printField("Guid", info.getGuid());
        printField("isFree", info.isFree());
        printField("isTrial", info.isTrial());
        printField("LicenseKeys", info.getLicenseKeys().toString());
        printField("LicenseSignature", info.getLicenseSignature());
        printField("LicenseState", info.getLicenseState());
        printField("MasterGuid", info.getMasterGuid());
        printField("Mode", info.getMode());
        printField("OsBuild", info.getOsBuild());
        printField("OsVersion", info.getOsVersion());
        printField("ServerName", info.getServerName());
        printField("Version", info.getVersion());
    }

    void printSettings(Settings settings) {
        printEntity(settings);
        printField("SplunkDB", settings.getSplunkDB());
        printField("SplunkHome", settings.getSplunkHome());
        printField("EnableSplunkWebSSL", settings.getEnableSplunkWebSSL());
        printField("Host", settings.getHost());
        printField("HttpPort", settings.getHttpPort());
        printField("MgmtPort", settings.getMgmtPort());
        printField("Pass4SymmKey", settings.getPass4SymmKey());
        printField("ServerName", settings.getServerName());
        printField("SessionTimeout", settings.getSessionTimeout());
        printField("StartWebServer", settings.getStartWebServer());
        printField("TrustedIP", settings.getTrustedIP());
    }

    public void run() throws Exception {
        Service service = connect();

        System.out.print("\n# Info");
        printServiceInfo(service.getInfo());

        System.out.print("\n# Settings");
        printSettings(service.getSettings());

        System.out.print("\n# Applications");
        for (Entity app : service.getApplications().values())
            printApplication((Application)app);

        System.out.print("\n# Configs");
        printResources(service.getConfigs());

        System.out.print("\n# Capabilities\n");
        for (String capability : service.getCapabilities())
            System.out.println(capability);

        System.out.print("\n# DeploymentClient");
        printEntity(service.getDeploymentClient());

        System.out.print("\n# DeploymentServers");
        printEntities(service.getDeploymentServers());

        System.out.print("\n# DeploymentServerClasses");
        printEntities(service.getDeploymentServerClasses());

        System.out.print("\n# DeploymentTenants");
        printEntities(service.getDeploymentTenants());

        System.out.print("\n# DistributedConfiguration");
        printEntity(service.getDistributedConfiguration());

        System.out.print("\n# EventTypes");
        printEntities(service.getEventTypes());

        System.out.print("\n# Indexes");
        for (Entity index : service.getIndexes().values())
            printIndex((Index)index);

        System.out.print("\n# Jobs");
        for (Entity job : service.getJobs().values())
            printJob((Job)job);

        System.out.print("\n# LicenseGroups");
        printEntities(service.getLicenseGroups());

        System.out.print("\n# LicenseMessages");
        printEntities(service.getLicenseMessages());

        System.out.print("\n# LicensePools");
        printEntities(service.getLicensePools());

        System.out.print("\n# LicenseSlaves");
        printEntities(service.getLicenseSlaves());

        System.out.print("\n# LicenseStacks");
        printEntities(service.getLicenseStacks());

        System.out.print("\n# Licenses");
        printEntities(service.getLicenses());

        System.out.print("\n# Loggers");
        printEntities(service.getLoggers());

        System.out.print("\n# Messages");
        for (Message message : service.getMessages().values())
            printMessage(message);

        System.out.print("\n# Passwords");
        printEntities(service.getPasswords());

        System.out.print("\n# Roles");
        printEntities(service.getRoles());

        System.out.print("\n# Saved Searches");
        for (Entity search : service.getSearches().values())
            printSavedSearch((SavedSearch)search);

        System.out.print("\n# Users");
        for (User user : service.getUsers().values())
            printUser(user);
    }
}

