package com.east.east_utils.utils.printer;

import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintManager;

import androidx.annotation.RequiresApi;

import com.east.east_utils.utils.Utils;
import com.east.east_utils.utils.printer.printeradapter.MyPrintPdfAdapter;

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 *  @description: 打印工具类
 *  @author: jamin
 *  @date: 2020/4/22
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class PrintUtils {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void print(String filePath){
        PrintManager printManager = (PrintManager) Utils.getContext().getSystemService(Context.PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(PrintAttributes.COLOR_MODE_COLOR);
        printManager.print("test pdf print",
                /*new MyPrintAdapter(this,filePath)*/ new MyPrintPdfAdapter(filePath),
                builder.build());
    }
}
