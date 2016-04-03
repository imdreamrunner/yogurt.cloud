package cloud.yogurt.shared.message;

import java.io.IOException;
import java.io.SequenceInputStream;

public class CombineDataLoader extends MessageDataLoader {
    SequenceInputStream sequenceInputStream;

    public CombineDataLoader(MessageDataLoader loader1, MessageDataLoader loader2) {
        sequenceInputStream = new SequenceInputStream(loader1, loader2);
    }

    @Override
    public int read() throws IOException {
        return sequenceInputStream.read();
    }
}
