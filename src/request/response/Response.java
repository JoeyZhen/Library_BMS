package request.response;

/**
 * Class representing a response. The base class (this) only
 * returns a static string given to the constructor.
 *
 * @author Zachary Cook
 */
public class Response {
    private String response;

    /**
     * Creates the response.
     *
     * @param response the response to give, including ending characters.
     */
    public Response(String response) {
        this.response = response;
    }

    /**
     * Returns the request.response.
     *
     * @return the request.response.
     */
    public String getResponse() {
        return this.response;
    }
}
