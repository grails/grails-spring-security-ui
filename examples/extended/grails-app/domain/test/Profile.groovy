package test

class Profile implements Serializable {

    private static final long serialVersionUID = 1

    String myQuestion
    String myAnswer
    String myQuestion2
    String myAnswer2
    User user
    static belongsTo = [ user: User]
    static constraints = {
    }
}
