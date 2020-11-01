package hello;

public class SizedMessage {

    private String body;
    private int sizeInMB;
    public int getSizeInMB() {return sizeInMB;}
    public void setSizeInMB(int size) {this.sizeInMB = size;}

    public SizedMessage() {
    }

    public SizedMessage(int bodySizeInMB) {

        this.sizeInMB = bodySizeInMB;
        int sizeInBytes = (bodySizeInMB*1000*1000);
        int numOfCharsNeeded = sizeInBytes/2;
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < numOfCharsNeeded; i++) {
            strBuilder.append('a');
        }

        this.body = strBuilder.toString();
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("message size in MB: %s", getSizeInMB());
    }

}