package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceSheet {
    private int id;
    private int userId;
    private LocalDate date;
    private BigDecimal cashDeposits;
    private BigDecimal notesReceivable;
    private BigDecimal accountsReceivable;
    private BigDecimal securities;
    private BigDecimal inventory;
    private BigDecimal allowanceDoubtful;
    private BigDecimal buildings;
    private BigDecimal land;
    private BigDecimal notesPayable;
    private BigDecimal accountsPayable;
    private BigDecimal shortTermLoans;
    private BigDecimal longTermLoans;
    private BigDecimal capitalStock;
    private BigDecimal retainedEarnings;

    // コンストラクタ、ゲッター、セッターを追加

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getCashDeposits() {
        return cashDeposits;
    }

    public void setCashDeposits(BigDecimal cashDeposits) {
        this.cashDeposits = cashDeposits;
    }

	public BigDecimal getNotesReceivable() {
		return notesReceivable;
	}

	public void setNotesReceivable(BigDecimal notesReceivable) {
		this.notesReceivable = notesReceivable;
	}

	public BigDecimal getAccountsReceivable() {
		return accountsReceivable;
	}

	public void setAccountsReceivable(BigDecimal accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	public BigDecimal getSecurities() {
		return securities;
	}

	public void setSecurities(BigDecimal securities) {
		this.securities = securities;
	}

	public BigDecimal getInventory() {
		return inventory;
	}

	public void setInventory(BigDecimal inventory) {
		this.inventory = inventory;
	}

	public BigDecimal getAllowanceDoubtful() {
		return allowanceDoubtful;
	}

	public void setAllowanceDoubtful(BigDecimal allowanceDoubtful) {
		this.allowanceDoubtful = allowanceDoubtful;
	}

	public BigDecimal getBuildings() {
		return buildings;
	}

	public void setBuildings(BigDecimal buildings) {
		this.buildings = buildings;
	}

	public BigDecimal getLand() {
		return land;
	}

	public void setLand(BigDecimal land) {
		this.land = land;
	}

	public BigDecimal getNotesPayable() {
		return notesPayable;
	}

	public void setNotesPayable(BigDecimal notesPayable) {
		this.notesPayable = notesPayable;
	}

	public BigDecimal getAccountsPayable() {
		return accountsPayable;
	}

	public void setAccountsPayable(BigDecimal accountsPayable) {
		this.accountsPayable = accountsPayable;
	}

	public BigDecimal getShortTermLoans() {
		return shortTermLoans;
	}

	public void setShortTermLoans(BigDecimal shortTermLoans) {
		this.shortTermLoans = shortTermLoans;
	}

	public BigDecimal getLongTermLoans() {
		return longTermLoans;
	}

	public void setLongTermLoans(BigDecimal longTermLoans) {
		this.longTermLoans = longTermLoans;
	}

	public BigDecimal getCapitalStock() {
		return capitalStock;
	}

	public void setCapitalStock(BigDecimal capitalStock) {
		this.capitalStock = capitalStock;
	}

	public BigDecimal getRetainedEarnings() {
		return retainedEarnings;
	}

	public void setRetainedEarnings(BigDecimal retainedEarnings) {
		this.retainedEarnings = retainedEarnings;
	}

    // その他のゲッター・セッターも同様に定義

}