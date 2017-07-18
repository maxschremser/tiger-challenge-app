package com.schremser.challenger.domains;


import java.util.Date;

/*
 * ExpenseInfo
 *
 * uses expense.ts
 *
 export class Expense {
  id: number;
  date: Date; // YYYYmmdd
  name: string;
  amount: number;
  type: string;
  owner: string;
 }

 */
public class ExpenseInfo extends ResourceItemBase {
    private String type;
    private Double amount;
    private Integer date; // YYYYMMDD format
    private String description;

    public ExpenseInfo() {}

    public ExpenseInfo(ExpenseInfo source) {
        super();
        type = source.type;
        date = source.date;
        description = source.description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer expenseDate) {
        date = expenseDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setAmount(Integer amount) {
        this.amount = Double.valueOf(amount);
    }
}
