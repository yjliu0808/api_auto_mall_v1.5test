package com.entity;

/**
 * @Author： Athena
 * @Date： 2025-03-21
 * @Desc： 回写响应数据映射的实体类
 **/
public class WriteBackData {
    //回写行号
    private int rowNum;
    //回写列号
    private int cellNum;
    //回写开始的sheet
    private int startSheetIndex;
    //回写内容
    private String content;

    public WriteBackData() {
    }

    public int getRowNum() {
        return rowNum;
    }

    public WriteBackData(int rowNum, int cellNum, int startSheetIndex, String content) {
        this.rowNum = rowNum;
        this.cellNum = cellNum;
        this.startSheetIndex = startSheetIndex;
        this.content = content;
    }

    @Override
    public String toString() {
        return "WriteBackData{" +
                "rowNum=" + rowNum +
                ", cellNum=" + cellNum +
                ", startSheetIndex=" + startSheetIndex +
                ", content='" + content + '\'' +
                '}';
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public int getStartSheetIndex() {
        return startSheetIndex;
    }

    public void setStartSheetIndex(int startSheetIndex) {
        this.startSheetIndex = startSheetIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

