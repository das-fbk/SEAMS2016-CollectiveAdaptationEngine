package it.infn.gssi.experiment;

public class ExperimentResult extends Loggable {

    private int id;

    private long dv1 = 0; // execution-time
    private long dv2 = 0; // memory consumption
    private double dv3 = 0;// CPU utilization
    private int dv4 = 0; // min Depth of resolution trees
    private int dv5 = 0; // max Depth of resolution trees
    private int dv6 = 0; // average depth resolution trees
    private int dv7 = 0; // min Extent of resolution trees
    private int dv8 = 0; // max Extent of resolution trees
    private int dv9 = 0; // average Extent of resolution trees
    private int dv10 = 0; // cross-ensembles issues

    public ExperimentResult() {
	super();
    }

    public ExperimentResult(int id, long dv1, long dv2, double dv3, int dv4, int dv5, int dv6, int dv7, int dv8,
	    int dv9, int dv10) {
	this.id = id;
	this.dv1 = dv1;
	this.dv2 = dv2;
	this.dv3 = dv3;
	this.dv4 = dv4;
	this.dv5 = dv5;
	this.dv6 = dv6;
	this.dv7 = dv7;
	this.dv8 = dv8;
	this.dv9 = dv9;
	this.dv10 = dv10;
    }

    public String getCsvFileHeader(String commaDelimiter) {
	String result = "id" + commaDelimiter + "dv1" + commaDelimiter + "dv2" + commaDelimiter + "dv3" + commaDelimiter
		+ "dv4" + commaDelimiter + "dv5" + commaDelimiter + "dv6" + commaDelimiter + "dv7" + commaDelimiter
		+ "dv8" + commaDelimiter + "dv9" + commaDelimiter + "dv10";
	return result;
    }

    public String toCsv(String commaDelimiter) {
	String result = "";
	result += this.id + commaDelimiter;
	result += this.dv1 + commaDelimiter;
	result += this.dv2 + commaDelimiter;
	result += this.dv3 + commaDelimiter;
	result += this.dv4 + commaDelimiter;
	result += this.dv5 + commaDelimiter;
	result += this.dv6 + commaDelimiter;
	result += this.dv7 + commaDelimiter;
	result += this.dv8 + commaDelimiter;
	result += this.dv9 + commaDelimiter;
	result += this.dv10;
	return result;
    }

    @Override
    public String toString() {
	return "ExperimentResult [id=" + id + ", dv1=" + dv1 + ", dv2=" + dv2 + ", dv3=" + dv3 + ", dv4=" + dv4
		+ ", dv5=" + dv5 + ", dv6=" + dv6 + ", dv7=" + dv7 + ", dv8=" + dv8 + ", dv9=" + dv9 + ", dv10=" + dv10
		+ "]";
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public long getDv1() {
	return dv1;
    }

    public void setDv1(long dv1) {
	this.dv1 = dv1;
    }

    public long getDv2() {
	return dv2;
    }

    public void setDv2(long dv2) {
	this.dv2 = dv2;
    }

    public double getDv3() {
	return dv3;
    }

    public void setDv3(double dv3) {
	this.dv3 = dv3;
    }

    public int getDv4() {
	return dv4;
    }

    public void setDv4(int dv4) {
	this.dv4 = dv4;
    }

    public int getDv5() {
	return dv5;
    }

    public void setDv5(int dv5) {
	this.dv5 = dv5;
    }

    public int getDv6() {
	return dv6;
    }

    public void setDv6(int dv6) {
	this.dv6 = dv6;
    }

    public int getDv7() {
	return dv7;
    }

    public void setDv7(int dv7) {
	this.dv7 = dv7;
    }

    public long getDv8() {
	return dv8;
    }

    public void setDv8(int dv8) {
	this.dv8 = dv8;
    }

    public double getDv9() {
	return dv9;
    }

    public void setDv9(int dv9) {
	this.dv9 = dv9;
    }

    public double getDv10() {
	return dv10;
    }

    public void setDv10(int dv10) {
	this.dv10 = dv10;
    }
}
