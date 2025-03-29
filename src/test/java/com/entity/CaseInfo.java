package com.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Author： Athena
 * @Date： 2025-03-20
 * @Desc： 映射excel的java实体类
 **/
public class CaseInfo {
    @Excel(name = "用例编号" )
    private int caseId;
    @Excel(name = "接口模块名称")
    private String interfaceName;
    @Excel(name = "用例描述" )
    private String caseDesc;
    @Excel(name = "url" )
    private String url;
    @Excel(name = "参数类型" )
    private String contentType;
    @Excel(name = "请求方式" )
    private String type;
    @Excel(name = "请求参数" )
    private String params;
    @Excel(name = "期望结果" )
    private String expectedResult;
    @Excel(name = "sql" )
    private String sql;
    @Excel(name = "预期SQL断言差值")
    private Integer   expectedSqlDiff;
    public CaseInfo() {
    }

    @Override
    public String toString() {
        return caseDesc; // 👈 只返回用例描述
    }

    public CaseInfo(int caseId, String interfaceName, String caseDesc, String url, String contentType, String type, String params, String expectedResult, String sql, Integer expectedSqlDiff) {
        this.caseId = caseId;
        this.interfaceName = interfaceName;
        this.caseDesc = caseDesc;
        this.url = url;
        this.contentType = contentType;
        this.type = type;
        this.params = params;
        this.expectedResult = expectedResult;
        this.sql = sql;
        this.expectedSqlDiff = expectedSqlDiff;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getExpectedSqlDiff() {
        return expectedSqlDiff;
    }

    public void setExpectedSqlDiff(Integer expectedSqlDiff) {
        this.expectedSqlDiff = expectedSqlDiff;
    }
}
