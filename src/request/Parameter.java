package request;

/**
 * Class representing parameter name and type.
 *
 * @author Zachary Cook
 */
public class Parameter {
    private ParameterType parameterType;
    private String name;

    /**
     * Types of parameters.
     */
    public enum ParameterType {
        STRING,
        LIST_OF_STRINGS,
        REMAINING_STRINGS,
        INTEGER,
        LIST_OF_INTEGERS,
        REMAINING_INTEGERS,
    }

    /**
     * Creates a parameter.
     *
     * @param name the name of the parameter.
     * @param parameterType the type of the parameter.
     */
    public Parameter(String name,ParameterType parameterType) {
        this.parameterType = parameterType;
        this.name = name;
    }

    /**
     * Returns the name.
     *
     * @return the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the type.
     *
     * @return the type.
     */
    public ParameterType getType() {
        return this.parameterType;
    }
}
