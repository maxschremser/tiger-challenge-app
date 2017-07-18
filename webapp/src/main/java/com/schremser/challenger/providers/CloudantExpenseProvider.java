package com.schremser.challenger.providers;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonObject;
import com.schremser.challenger.domains.ExpenseCreationInfo;
import com.schremser.challenger.domains.ExpenseInfo;
import com.schremser.challenger.domains.ExpenseType;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


public class CloudantExpenseProvider implements IExpenseProvider {
    private final String DATE_PATTERN = "yyyyMMdd";
    private final String DATE_TIME_PATTERN = "yyyyMMdd.HHmm";
    private final String TIME_PATTERN = "#0.0000";
    private final String DECIMAL_PATTERN = "#0.00";
    private AtomicInteger i_nextExpenseIdnb = new AtomicInteger(1);
    private Map<String, ExpenseInfo> i_expenseByIdnb;
    private Map<String, Map<String, ExpenseInfo>> i_expenseByDate;
    private Map<ExpenseType, Map<String, ExpenseInfo>> i_expenseByTypeAndName;

    private static CloudantExpenseProvider s_instance;
    private static CloudantClient cloudantClient;
    private Database db = null;
    private static final String databaseName = "challenge";

    public static synchronized CloudantExpenseProvider instance() throws ParseException {
        if (s_instance == null)
            s_instance = new CloudantExpenseProvider();
        return s_instance;
    }

    private CloudantExpenseProvider() throws ParseException {
        cloudantClient = createClient();
        if (cloudantClient != null) {
            db = cloudantClient.database(databaseName, true);
        }
    }

    public Database getDB() {
        return db;
    }

    private static CloudantClient createClient() {
        String url;

        if (System.getenv("VCAP_SERVICES") != null) {
            // When running in Bluemix, the VCAP_SERVICES env var will have the credentials for all bound/connected services
            // Parse the VCAP JSON structure looking for cloudant.
            JsonObject cloudantCredentials = VCAPHelper.getCloudCredentials("cloudant");
            if (cloudantCredentials == null) {
                System.out.println("No cloudant database service bound to this application");
                return null;
            }
            url = cloudantCredentials.get("url").getAsString();
        } else {
            System.out.println("Running locally. Looking for credentials in cloudant.json");
            url = VCAPHelper.getLocalCredentials("cloudant.json").get("url").getAsString();
            if (url == null || url.length() == 0) {
                System.out.println("To use a database, set the Cloudant url in src/main/resources/cloudant.json");
                return null;
            }
        }

        try {
            System.out.println("Connecting to Cloudant");
            CloudantClient client = ClientBuilder.url(new URL(url)).build();
            return client;
        } catch (Exception e) {
            System.out.println("Unable to connect to database");
            //e.printStackTrace();
            return null;
        }
    }


    public List<ExpenseInfo> getAll() {
        List<ExpenseInfo> docs;
        try {
            docs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(ExpenseInfo.class);
        } catch (IOException e) {
            return null;
        }
        return docs;
    }

    public ExpenseInfo get(String id) {
        return db.find(ExpenseInfo.class, id);
    }

    public ExpenseInfo persist(ExpenseInfo td) {
        String id = db.save(td).getId();
        return db.find(ExpenseInfo.class, id);
    }

    public ExpenseInfo update(String id, ExpenseInfo newVisitor) {
        ExpenseInfo visitor = db.find(ExpenseInfo.class, id);
        visitor.setName(newVisitor.getName());
        db.update(visitor);
        return db.find(ExpenseInfo.class, id);

    }

    public void delete(String id) {
        ExpenseInfo visitor = db.find(ExpenseInfo.class, id);
        db.remove(visitor);
    }

    public int count() throws Exception {
        return getAll().size();
    }

    @Override
    public ExpenseInfo createExpense(String loginSessionId, ExpenseCreationInfo expenseInfo) {
        String id = db.save(expenseInfo).getId();
        return db.find(ExpenseInfo.class, id);
    }

    @Override
    public ExpenseInfo updateExpense(String loginSessionId, String id, ExpenseInfo newExpenseInfo) {
        ExpenseInfo expenseInfo = db.find(ExpenseInfo.class, id);
        expenseInfo.setName(newExpenseInfo.getName());
        db.update(expenseInfo);
        return db.find(ExpenseInfo.class, id);
    }

    @Override
    public void deleteExpense(String loginSessionId, String id) {
        ExpenseInfo visitor = db.find(ExpenseInfo.class, id);
        db.remove(visitor);
    }

    @Override
    public ExpenseInfo getExpense(String loginSessionId, String id) {
        return db.find(ExpenseInfo.class, id);
    }

    @Override
    public Collection<ExpenseInfo> getExpenses(String loginSessionId) {
        return getAll();
    }

    @Override
    public Collection<ExpenseInfo> getExpensesByType(String loginSessionId, ExpenseType expenseType) {
        return Collections.emptyList();
    }

    @Override
    public Collection<ExpenseInfo> getTodaysExpenses(String loginSessionId) {
        // return db.find(ExpenseInfo.class, );
        return Collections.emptyList();
    }

    @Override
    public Collection<ExpenseInfo> getTodaysExpensesByType(String loginSessionId, ExpenseType expenseType) {
        return Collections.emptyList();
    }

    private synchronized void addExpense(String loginSessionId, ExpenseInfo expenseInfo) {
        db.post(expenseInfo);
    }
}
