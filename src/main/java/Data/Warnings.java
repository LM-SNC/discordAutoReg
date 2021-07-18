package Data;

public enum Warnings {
    EMAIL_ALREADY_REGISTERED("Email уже зарегестрирован"),
    USERNAME_TOO_MANY_USERS("Login уже зарегестрирован"),
    BASE_TYPE_MIN_LENGTH("Пароль слишком короткий");

    private String title;

    Warnings(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Warnings{" +
                "title='" + title + '\'' +
                '}';
    }
}
