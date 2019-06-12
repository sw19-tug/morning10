package at.tugraz.ist.swe.cheat;

import org.junit.Test;

import java.util.Date;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;
import at.tugraz.ist.swe.cheat.util.ConverterClassByte;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static org.junit.Assert.assertEquals;

public class ConverterClassTest {


    @Test
    public void convertTest()
    {

        ChatMessage message = new ChatMessage(1, "User","Message",new Date());
        CustomMessage fullmessage = new CustomMessage(STATE_CONNECTED,
                new Device("test", "00:00:00:00"),message);

        try {
            byte[] buffer = ConverterClassByte.toByteArray(fullmessage);
            CustomMessage customMessage = (CustomMessage) ConverterClassByte.toObject(buffer);

            assertEquals(fullmessage.getDevice().getDevice_name(), customMessage.getDevice().getDevice_name());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
