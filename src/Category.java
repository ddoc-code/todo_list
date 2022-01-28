public enum Category {
    RED("\033[0;31m"), WHITE("\033[0;37m"), BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"), YELLOW("\033[0;33m"), GREEN("\033[0;32m");

    //colour string variable
    private String colour;

    //constructor
    Category(String s) {
        this.colour = s;
    }

    //colour getter
    public String getColour() {
        return colour;
    }
}
