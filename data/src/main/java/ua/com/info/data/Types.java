package ua.com.info.data;

import com.google.gson.annotations.SerializedName;

/**
 * Створив VM 26.08.2017.
 */

public enum Types {
    @SerializedName("-1")
    DBNull(-1),
    @SerializedName("0")
    BigInt(0),
    @SerializedName("1")
    Binary(1),
    @SerializedName("2")
    Bit(2),
    @SerializedName("3")
    Char(3),
    @SerializedName("4")
    DateTime(4),
    @SerializedName("5")
    Decimal(5),
    @SerializedName("6")
    Float(6),
    @SerializedName("7")
    Image(7),
    @SerializedName("8")
    Int(8),
    @SerializedName("9")
    Money(9),
    @SerializedName("10")
    NChar(10),
    @SerializedName("11")
    NText(11),
    @SerializedName("12")
    NVarChar(12),
    @SerializedName("13")
    Real(13),
    @SerializedName("14")
    UniqueIdentifier(14),
    @SerializedName("15")
    SmallDateTime(15),
    @SerializedName("16")
    SmallInt(16),
    @SerializedName("17")
    SmallMoney(17),
    @SerializedName("18")
    Text(18),
    @SerializedName("19")
    Timestamp(19),
    @SerializedName("20")
    TinyInt(20),
    @SerializedName("21")
    VarBinary(21),
    @SerializedName("22")
    VarChar(22),
    @SerializedName("23")
    Variant(23),
    @SerializedName("25")
    Xml(25),
    @SerializedName("29")
    Udt(29),
    @SerializedName("30")
    Structured(30),
    @SerializedName("31")
    Date(31),
    @SerializedName("32")
    Time(32),
    @SerializedName("33")
    DateTime2(33),
    @SerializedName("34")
    DateTimeOffset(34);

    private int _value;

    Types(int Value) {
        this._value = Value;
    }

    public int getValue() {
        return _value;
    }

    public static final int DBNULL = -1;
    public static final int BIGINT = 0;
    public static final int BINARY = 1;
    public static final int BIT = 2;
    public static final int CHAR = 3;
    public static final int DATETIME = 4;
    public static final int DECIMAL = 5;
    public static final int FLOAT = 6;
    public static final int IMAGE = 7;
    public static final int INT = 8;
    public static final int MONEY = 9;
    public static final int NCHAR = 10;
    public static final int NTEXT = 11;
    public static final int NVARCHAR = 12;
    public static final int REAL = 13;
    public static final int UNIQUEIDENTIFIER = 14;
    public static final int SMALLDATETIME = 15;
    public static final int SMALLINT = 16;
    public static final int SMALLMONEY = 17;
    public static final int TEXT = 18;
    public static final int TIMESTAMP = 19;
    public static final int TINYINT = 20;
    public static final int VARBINARY = 21;
    public static final int VARCHAR = 22;
    public static final int VARIANT = 23;
    public static final int XML = 25;
    public static final int UDT = 29;
    public static final int STRUCTURED = 30;
    public static final int DATE = 31;
    public static final int TIME = 32;
    public static final int DATETIME2 = 33;
    public static final int DATETIMEOFFSET = 34;
}
