// BalanceService.aidl
package com.chetan.commonapp.balanceServiceCommon;
import com.chetan.commonapp.balanceServiceCommon.DailyCash;
// Declare any non-default types here with import statements
//import DailyCash;
interface BalanceService {
    boolean createDatabase();
    DailyCash[] dailyCash(int yyyy, int mm, int dd, int workingDays);
    int calculate(int num1, int num2);
}
