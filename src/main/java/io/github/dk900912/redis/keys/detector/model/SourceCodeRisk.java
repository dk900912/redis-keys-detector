package io.github.dk900912.redis.keys.detector.model;

public class SourceCodeRisk {

    private String sourceCodeType;

    private String sourceCodeName;

    private int riskLineNum;

    private String risk;


    public SourceCodeRisk(String sourceCodeName, int riskLineNum, String risk) {
        this.sourceCodeName = sourceCodeName;
        this.riskLineNum = riskLineNum;
        this.risk = risk;
    }

    public String getSourceCodeType() {
        return sourceCodeType;
    }

    public void setSourceCodeType(String sourceCodeType) {
        this.sourceCodeType = sourceCodeType;
    }

    public String getSourceCodeName() {
        return sourceCodeName;
    }

    public void setSourceCodeName(String sourceCodeName) {
        this.sourceCodeName = sourceCodeName;
    }

    public int getRiskLineNum() {
        return riskLineNum;
    }

    public void setRiskLineNum(int riskLineNum) {
        this.riskLineNum = riskLineNum;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
