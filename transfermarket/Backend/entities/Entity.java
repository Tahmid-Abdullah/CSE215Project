package pkg.java.project.transfermarket.Backend.entities;

public abstract class Entity {
    private String name;
    private String password;

    public Entity(String name, String password) {
        this.name=name;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract String toString();
}
