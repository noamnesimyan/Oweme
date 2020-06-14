package com.example.oweme;

import com.google.firebase.auth.FirebaseAuth;

public class Util {


    public static void addDebt(MyDataBase myLocalDB, Expense newExpense, boolean permanent) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String[] members = newExpense.getMembers().split(",");
        double bill = newExpense.getAmount() / members.length;

        if (permanent)
            updateDebtForUser(newExpense, mAuth, myLocalDB, true, newExpense.getOwner(), bill);
        else{
            for (int i = 0; i < members.length; i++){
                if(!(mAuth.getCurrentUser().getUid().equals(newExpense.getOwner()) && mAuth.getCurrentUser().getUid().equals(members[i]))) {
                    updateDebtForUser(newExpense, mAuth, myLocalDB, false, members[i], bill);
                }
            }
        }

        myLocalDB.deleteDebt(); // delete debt if amount is 0
    }

    private static void updateDebtForUser(Expense newExpense, FirebaseAuth mAuth, MyDataBase myLocalDB, boolean permanent, String uid, double bill) {
        Debt newDebt = myLocalDB.getDebtByUid(uid, permanent);
        if (newDebt == null) {
            if(mAuth.getCurrentUser().getUid().equals(newExpense.getOwner())) {
                newDebt = new Debt(uid, bill, permanent);
            } else {
                newDebt = new Debt(uid, -bill, permanent);
            }
        }
        else {
            if(mAuth.getCurrentUser().getUid().equals(newExpense.getOwner())) {
                newDebt.setAmount(newDebt.getAmount() + bill);
            } else {
                newDebt.setAmount(newDebt.getAmount() - bill);
            }
        }
        myLocalDB.updateDebt(newDebt);

    }
}
