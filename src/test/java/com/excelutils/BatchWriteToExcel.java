package com.excelutils;

import com.entity.WriteBackData;
import com.loggerutil.BaseLogger;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： 批量回写响应结果到Excel中
 **/
public class BatchWriteToExcel extends BaseLogger {

    // 用于收集待回写数据
    private static final List<WriteBackData> writeBackDataList = new ArrayList<>();

    /**
     * 添加一条回写数据
     */
    public static void addWriteBackData(int rowNum, int cellNum, int sheetIndex, String content) {
        WriteBackData writeBackData = new WriteBackData(rowNum, cellNum, sheetIndex, content);
        writeBackDataList.add(writeBackData);
    }

    /**
     * 批量写入数据到 Excel
     */
    public static void batchWriteToExcel(String excelPath) {
        logger.info("准备批量写入 Excel，文件路径：" + excelPath);

        try (
                FileInputStream fis = new FileInputStream(excelPath);
                Workbook workbook = WorkbookFactory.create(fis)
        ) {
            for (WriteBackData data : writeBackDataList) {
                Sheet sheet = workbook.getSheetAt(data.getStartSheetIndex());
                Row row = sheet.getRow(data.getRowNum());
                if (row == null) {
                    row = sheet.createRow(data.getRowNum());
                }
                Cell cell = row.getCell(data.getCellNum(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(data.getContent());
            }

            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
                logger.info("写入 Excel 成功，共写入 " + writeBackDataList.size() + " 条数据。");
            }

        } catch (IOException e) {
            logger.error("写入 Excel 文件失败: " + excelPath, e);
            throw new RuntimeException("写入 Excel 文件失败: " + excelPath, e);
        } finally {
            // 清空写回列表，避免脏数据干扰下次写入
            writeBackDataList.clear();
        }
    }
}
