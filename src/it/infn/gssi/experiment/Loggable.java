package it.infn.gssi.experiment;

public abstract class Loggable {

    abstract String toCsv(String commaDelimiter);

    abstract String getCsvFileHeader(String commaDelimiter);

}
