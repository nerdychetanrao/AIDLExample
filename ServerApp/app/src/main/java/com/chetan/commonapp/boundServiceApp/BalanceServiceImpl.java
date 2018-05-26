package com.chetan.commonapp.boundServiceApp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.chetan.commonapp.balanceServiceCommon.DailyCash;
import com.chetan.commonapp.balanceServiceCommon.BalanceService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class BalanceServiceImpl extends Service {
    public BalanceServiceImpl() {
    }
    TreasuryDatabase myDb;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    BalanceService.Stub mBinder = new BalanceService.Stub() {
        @Override
        public boolean createDatabase() throws RemoteException {
            myDb = new TreasuryDatabase(getApplicationContext());
            try{
                InputStream inputStream = getApplicationContext().getResources().getAssets()
                        .open("treasury-io-final.txt", Context.MODE_WORLD_READABLE);
                //InputStream inputStream = getResources().openRawResource(R.raw.treasuryiofinal);
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
                String eachline = bufferedReader.readLine();
                while (eachline != null) {
                    // `the words in the file are separated by space`, so to get each words
                    String[] words = eachline.split(",");
                    int year = Integer.parseInt(words[0]);
                    int month = Integer.parseInt(words[1]);
                    int date = Integer.parseInt(words[2]);
                    String day = words[3];
                    int startamt = Integer.parseInt(words[4]);
                    int endamt = Integer.parseInt(words[5]);
                    myDb.insertData(year, month, date, day, startamt, endamt);
                    eachline = bufferedReader.readLine();
                }
            }catch (Exception e){
                Log.e("T", ""+e.toString());
            }
            return myDb.statusOfDB();
        }
        @Override
        public DailyCash[] dailyCash(int yyyy, int mm, int dd, int workingDays) throws RemoteException {
            int count = 0;
            Log.i("I", "in daily cash IMPL ");
            Cursor cursor = myDb.getDailyData(yyyy, mm, dd, workingDays);
            Log.i("I", "CURSOR COUNT ROWS " + String.valueOf(cursor.getCount()));
            DailyCash[] dcArray = new DailyCash[cursor.getCount()];
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        Log.i("I", "PArsing rows ROWS " );
                        String[] arrData = new String[cursor.getColumnCount()];
                        arrData[0] = cursor.getString(0); // YYYY
                        arrData[1] = cursor.getString(1); // MM
                        arrData[2] = cursor.getString(2); // DD
                        arrData[3] = cursor.getString(3); // DAY
                        arrData[4] = cursor.getString(4); // START
                        arrData[5] = cursor.getString(5); // END
                        Log.i("I", "DATA FROM DB " + arrData[0] + arrData[1] + arrData[2] + arrData[3] + arrData[4] + arrData[5]);
                        int diff = Integer.parseInt(cursor.getString(5)) - Integer.parseInt(cursor.getString(4));
                        DailyCash dcItem = new DailyCash(Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(0)), diff, cursor.getString(3));
                        dcArray[count] = dcItem;
                        Log.i("I", "Assigned to ARRAY " );
                        count += 1;

                    }
                } catch (Exception e)
                {
                    Log.i("I", "Exception DB");
                }
                }
            Log.i("I", "return arrayS ");
            return dcArray;
        }

        @Override
        public int calculate(int num1, int num2) {
            return num1+num2;
        }
    };


}
