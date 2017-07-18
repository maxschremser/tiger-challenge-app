package com.schremser.challenger.providers;

import com.schremser.challenger.domains.ExpenseCreationInfo;
import com.schremser.challenger.domains.ExpenseInfo;
import com.schremser.challenger.domains.ExpenseType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


public class MockExpenseProvider implements IExpenseProvider {
  private final String DATE_PATTERN = "yyyyMMdd";
  private final String DATE_TIME_PATTERN = "yyyyMMdd.HHmm";
  private final String TIME_PATTERN = "#0.0000";
  private final String DECIMAL_PATTERN = "#0.00";
  private AtomicInteger i_nextExpenseIdnb = new AtomicInteger(1);
  private Map<String, ExpenseInfo> i_expenseByIdnb;
  private Map<String, Map<String, ExpenseInfo>> i_expenseByDate;
  private Map<String, Map<String, ExpenseInfo>> i_expenseByTypeAndName;

  private static MockExpenseProvider s_instance;

  public static synchronized MockExpenseProvider instance() throws ParseException {
    if (s_instance == null)
      s_instance = new MockExpenseProvider();
    return s_instance;
  }

  private MockExpenseProvider() throws ParseException {
    i_expenseByIdnb = new LinkedHashMap<String, ExpenseInfo>();
    i_expenseByDate = new HashMap<String, Map<String, ExpenseInfo>>();
    i_expenseByTypeAndName = new HashMap<String, Map<String, ExpenseInfo>>();
    createSampleExpenses();
  }

  @Override
  public ExpenseInfo createExpense(String loginSessionId, ExpenseCreationInfo expenseInfo) {
    expenseInfo.setId(null);
    addExpense(loginSessionId, expenseInfo);
    return expenseInfo;
  }

  @Override
  public ExpenseInfo updateExpense(String loginSessionId, String id, ExpenseInfo expenseInfo) {
    if (!i_expenseByIdnb.containsKey(id))
      throw new ResourceNotFoundException("Expense", id);
    expenseInfo.setId(id);
    i_expenseByIdnb.put(id, expenseInfo);
    return expenseInfo;
  }

  @Override
  public void deleteExpense(String loginSessionId, String id) {
    if (!i_expenseByIdnb.containsKey(id))
      throw new ResourceNotFoundException("Expense", id);
    removeExpense(id);
  }

  @Override
  public ExpenseInfo getExpense(String loginSessionId, String id) {
    if (!i_expenseByIdnb.containsKey(id))
      throw new ResourceNotFoundException("Expense", id);
    return i_expenseByIdnb.get(id);
  }

  @Override
  public Collection<ExpenseInfo> getExpenses(String loginSessionId) {
    return i_expenseByIdnb.values();
  }

  @Override
  public Collection<ExpenseInfo> getExpensesByType(String loginSessionId, ExpenseType expenseType) {
    Map<String, ExpenseInfo> typeDatasets = i_expenseByTypeAndName.get(expenseType);
    if (typeDatasets != null)
      return typeDatasets.values();
    else
      return Collections.emptyList();
  }

  @Override
  public Collection<ExpenseInfo> getTodaysExpenses(String loginSessionId) {
    Map<String, ExpenseInfo> dateDatasets = i_expenseByDate.get(new SimpleDateFormat(DATE_PATTERN).format(new Date()));
    if (dateDatasets != null)
      return dateDatasets.values();
    else
      return Collections.emptyList();
  }

  @Override
  public Collection<ExpenseInfo> getTodaysExpensesByType(String loginSessionId, ExpenseType expenseType) {
    return null;
  }

  private synchronized void addExpense(String loginSessionId, ExpenseInfo expenseInfo) {
    Integer dsIdnb = i_nextExpenseIdnb.getAndIncrement();
    expenseInfo.setId(dsIdnb.toString());
    i_expenseByIdnb.put(dsIdnb.toString(), expenseInfo);
    if (expenseHasDate(expenseInfo)) {
      Map<String, ExpenseInfo> dateExpenses = i_expenseByDate.get(new SimpleDateFormat(DATE_PATTERN).format(expenseInfo.getDate()));
      if (dateExpenses == null) {
        dateExpenses = new HashMap<String, ExpenseInfo>();
        i_expenseByDate.put(new SimpleDateFormat(DATE_PATTERN).format(expenseInfo.getDate()), dateExpenses);
      }
      dateExpenses.put(expenseInfo.getName(), expenseInfo);
    }
    if (expenseHasName(expenseInfo)) {
      Map<String, ExpenseInfo> typeExpenses = i_expenseByTypeAndName.get(expenseInfo.getType());
      if (typeExpenses == null) {
        typeExpenses = new HashMap<String, ExpenseInfo>();
        i_expenseByTypeAndName.put(expenseInfo.getType(), typeExpenses);
      }
      typeExpenses.put(expenseInfo.getName(), expenseInfo);
    }
  }

  private boolean expenseHasDate(ExpenseInfo expenseInfo) {
    return expenseInfo.getDate() != null;
  }

  private boolean expenseHasName(ExpenseInfo expenseInfo) {
    return expenseInfo.getName() != null && expenseInfo.getName().trim().length() > 0;
  }

  private synchronized void removeExpense(String id) {
    ExpenseInfo dsInfo = i_expenseByIdnb.get(id);
    if (dsInfo != null) {
      i_expenseByIdnb.remove(id);
      Map<String, ExpenseInfo> dateDatasets = i_expenseByDate.get(dsInfo.getDate());
      if (dateDatasets != null) {
        dateDatasets.remove(dsInfo.getName());
      }
    }
  }

  String[] expenseNames = {
      "Service 2017",
      "Shopping Lui&Max",
      "Sebastiano",
      "Latino (Suceava)",
      "Fitnessland",
      "Waschmaschine",
      "Internet",
      "Abschlussfeier"
  };
  String[] expenseTypes = {
      "car",
      "bicycle",
      "pizza",
      "bicycle",
      "pizza",
      "home",
      "home",
      "game-controller-a"
  };
  double[] expenseDates = {
      20170421.1236,
      20170421.1832,
      20170423.2142,
      20170424.1250,
      20170424.2223,
      20170425.1200,
      20170426.1841,
      20170428.1241
  };
  double[] expenseAmounts = {
      710.59,
      623.42,
      110,
      36.70,
      89,
      30,
      20,
      43.21
  };

  private void createSampleExpenses() throws ParseException {

    DecimalFormat dfTime = new DecimalFormat(TIME_PATTERN);
    DecimalFormatSymbols dfs = dfTime.getDecimalFormatSymbols();
    dfs.setDecimalSeparator('.');
    dfTime.setDecimalFormatSymbols(dfs);
    for (int i = 0; i < expenseTypes.length; i++) {
      ExpenseInfo ds = new ExpenseInfo();
      ds.setType(expenseTypes[i]);
      ds.setName(expenseNames[i]);
      String date = dfTime.format(expenseDates[i]);
      //ds.setDate(new SimpleDateFormat(DATE_TIME_PATTERN, Locale.ENGLISH).parse(date));
      ds.setOwner("maxi");
      ds.setDescription("maxis description");
      ds.setAmount(expenseAmounts[i]);
      addExpense(null, ds);

      ExpenseInfo ds2 = new ExpenseInfo();
      ds2.setType(expenseTypes[i]);
      ds2.setName("another " + expenseNames[i]);
      //ds2.setDate(new SimpleDateFormat(DATE_TIME_PATTERN).parse(date));
      ds2.setOwner("maxi");
      ds2.setAmount(expenseAmounts[i] + 3.25);
      ds2.setDescription("another description");
      addExpense(null, ds2);
    }

    ExpenseInfo templateDS = new ExpenseInfo();
    templateDS.setType("game-controller-a");
    templateDS.setName("PrePaid SIM");
    Calendar calendar = Calendar.getInstance();
    calendar.set(2017, 03, 20, 19, 38);
    //templateDS.setDate(calendar.getTime());
    templateDS.setOwner("maxi");
    templateDS.setDescription("new sim card from julia.");
    templateDS.setAmount(5);
    addExpense(null, templateDS);

    for (int i = 0; i < 8; i++) {
      ExpenseInfo expenseInfo = new ExpenseInfo();
      expenseInfo.setAmount(7.77 + Math.random() * 100);
      String HH = new DecimalFormat("00").format(ThreadLocalRandom.current().nextInt(0, 24));
      String mm = new DecimalFormat("00").format(ThreadLocalRandom.current().nextInt(0, 60));
      //expenseInfo.setDate(new SimpleDateFormat(DATE_TIME_PATTERN).parse(new SimpleDateFormat(DATE_PATTERN).format(new Date()) + "." + HH + mm));
      expenseInfo.setOwner("maxi");
      expenseInfo.setName("some " + expenseNames[i]);
      expenseInfo.setDescription("some maxi description");
      expenseInfo.setType(expenseTypes[i]);
      addExpense(null, expenseInfo);
    }
  }
}
