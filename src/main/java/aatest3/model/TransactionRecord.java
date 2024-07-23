package aatest3.model;

import aatest3.exception.InvalidTransactionRecordException;
import aatest3.util.FileSpecLoader;

import java.util.List;

public class TransactionRecord {
    private String recordCode;
    private String clientType;
    private String clientNumber;
    private String accountNumber;
    private String subAccountNumber;
    private String oppositePartyCode;
    private String productGroupCode;
    private String exchangeCode;
    private String symbol;
    private String expirationDate;
    private int quantityLong;
    private int quantityShort;
    private String exchBrokerFeeDec;
    private String exchBrokerFeeDC;
    private String exchBrokerFeeCurCode;
    private String clearingFeeDec;
    private String clearingFeeDC;
    private String clearingFeeCurCode;
    private String commission;
    private String commissionDC;
    private String commissionCurCode;
    private String transactionDate;
    private String futureReference;
    private String ticketNumber;
    private String externalNumber;
    private String transactionPriceDec;
    private String traderInitials;
    private String oppositeTraderId;
    private String openCloseCode;
    private String filler;

    public TransactionRecord() {}

    public TransactionRecord(String line, List<FileSpecLoader.FieldSpec> specs) throws InvalidTransactionRecordException {
        for (FileSpecLoader.FieldSpec spec : specs) {
            String value;
            // Check if the line is long enough for this field
            if (line.length() >= spec.end) {
                value = line.substring(spec.start, spec.end).trim();
            } else if (spec.name.equals("filler")) {
                // If the line is too short and the field is "filler", set a default value or skip
                value = ""; // Default value for optional "filler"
                this.filler = value;
                continue; // Skip further processing for "filler"
            } else {
                // For other fields, hrow an exception
                throw new InvalidTransactionRecordException("Line too short for required field: " + spec.name);
            }

            switch (spec.name) {
                case "recordCode":
                    this.recordCode = value;
                    break;
                case "clientType":
                    this.clientType = value;
                    break;
                case "clientNumber":
                    this.clientNumber = value;
                    break;
                case "accountNumber":
                    this.accountNumber = value;
                    break;
                case "subAccountNumber":
                    this.subAccountNumber = value;
                    break;
                case "oppositePartyCode":
                    this.oppositePartyCode = value;
                    break;
                case "productGroupCode":
                    this.productGroupCode = value;
                    break;
                case "exchangeCode":
                    this.exchangeCode = value;
                    break;
                case "symbol":
                    this.symbol = value;
                    break;
                case "expirationDate":
                    this.expirationDate = value;
                    break;
                case "quantityLong":
                    this.quantityLong = parseQuantity(value);
                    break;
                case "quantityShort":
                    this.quantityShort = parseQuantity(value);
                    break;
                case "exchBrokerFeeDec":
                    this.exchBrokerFeeDec = value;
                    break;
                case "exchBrokerFeeDC":
                    this.exchBrokerFeeDC = value;
                    break;
                case "exchBrokerFeeCurCode":
                    this.exchBrokerFeeCurCode = value;
                    break;
                case "clearingFeeDec":
                    this.clearingFeeDec = value;
                    break;
                case "clearingFeeDC":
                    this.clearingFeeDC = value;
                    break;
                case "clearingFeeCurCode":
                    this.clearingFeeCurCode = value;
                    break;
                case "commission":
                    this.commission = value;
                    break;
                case "commissionDC":
                    this.commissionDC = value;
                    break;
                case "commissionCurCode":
                    this.commissionCurCode = value;
                    break;
                case "transactionDate":
                    this.transactionDate = value;
                    break;
                case "futureReference":
                    this.futureReference = value;
                    break;
                case "ticketNumber":
                    this.ticketNumber = value;
                    break;
                case "externalNumber":
                    this.externalNumber = value;
                    break;
                case "transactionPriceDec":
                    this.transactionPriceDec = value;
                    break;
                case "traderInitials":
                    this.traderInitials = value;
                    break;
                case "oppositeTraderId":
                    this.oppositeTraderId = value;
                    break;
                case "openCloseCode":
                    this.openCloseCode = value;
                    break;
                case "filler": // Already handled above
                    break;
            }
        }
    }

    private int parseQuantity(String quantityStr) throws InvalidTransactionRecordException {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InvalidTransactionRecordException("Invalid quantity format.");
        }
    }

    public String getClientInformation() {
        return String.join("_", clientType, clientNumber, accountNumber, subAccountNumber);
    }

    public String getProductInformation() {
        return String.join("_", exchangeCode, productGroupCode, symbol, expirationDate);
    }

    public int getNetTransactionAmount() {
        return quantityLong - quantityShort;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public String getClientType() {
        return clientType;
    }

    public int getQuantityLong() {
        return quantityLong;
    }

    public int getQuantityShort() {
        return quantityShort;
    }
}
