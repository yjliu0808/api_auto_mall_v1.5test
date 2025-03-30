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
        logger.debug(String.format("📌 收集写回数据 -> sheet: %d, row: %d, col: %d, content: %s",
                sheetIndex, rowNum, cellNum, content));
    }

    /**
     * 获取当前待写入数据的数量（供调试用）
     */
    public static int getWriteBackDataSize() {
        return writeBackDataList.size();
    }

    /**
     * 批量写入数据到 Excel（默认输出日志）
     */
    public static void batchWriteToExcel(String excelPath) {
        batchWriteToExcel(excelPath, true);
    }

    /**
     * 批量写入数据到 Excel，可控制是否打印日志
     * @param excelPath Excel 文件路径
     * @param showLog 是否输出写入日志
     */
    public static void batchWriteToExcel(String excelPath, boolean showLog) {
        if (showLog) {
            logger.info("准备批量写入 Excel，文件路径：" + excelPath);
        }

        int writeSize = writeBackDataList.size();

        if (writeSize == 0) {
            if (showLog) {
                logger.info("✅ 所有用例执行完毕，批量回写 Excel 成功，共写入 0 条数据。\n");
            }
            return;
        }

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
            }

            if (showLog) {
                logger.info("✅ 所有用例执行完毕，批量回写 Excel 成功，共写入 " + writeSize + " 条数据。");
            }

        } catch (IOException e) {
            logger.error("写入 Excel 文件失败: " + excelPath, e);
            throw new RuntimeException("写入 Excel 文件失败: " + excelPath, e);
        } finally {
            if (showLog) {
                logger.debug("🧹 清空写回数据列表，当前写回数据如下：");
                for (WriteBackData data : writeBackDataList) {
                    logger.debug(String.format("sheet=%d, row=%d, col=%d, content=%s",
                            data.getStartSheetIndex(), data.getRowNum(), data.getCellNum(), data.getContent()));
                }
            }
            writeBackDataList.clear();
        }
    }
}
