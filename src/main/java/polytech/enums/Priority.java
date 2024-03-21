package polytech.enums;

public enum Priority {
    LOWEST(0),
    LOW(1),
    MIDDLE(2),
    HIGH(3);

    private final int value;

    Priority(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
