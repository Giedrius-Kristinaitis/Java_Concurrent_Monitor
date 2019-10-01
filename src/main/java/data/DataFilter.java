package data;

public class DataFilter implements FilterInterface<Data> {

    private static final char[] letters = new char[] {
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p',
            'r', 's', 't', 'v', 'z'
    };

    @Override
    public boolean filter(Data data) {
        return lettersContain(data.getResult().charAt(0));
    }

    private boolean lettersContain(char c) {
        for (char letter : letters) {
            if (letter == c) {
                return true;
            }
        }

        return false;
    }
}
