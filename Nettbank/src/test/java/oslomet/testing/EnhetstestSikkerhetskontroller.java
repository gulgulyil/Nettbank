package oslomet.testing;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhetsController {

    @InjectMocks
    //denne skal testes
    private Sikkerhet sikkerhetsController;

    @Mock
    //denne skal Mock'es
    private BankRepository bankRepository;

    @Mock
    //dene skal Mock'es
    private MockHttpSession mockHttpSession;

    @Before
    public void initSession() {

        Map<String, Object> objectMap = new HashMap<String, Object>();

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                String key = (String) invocationOnMock.getArguments()[0];
                return objectMap.get(key);
            }
        }).when(mockHttpSession).getAttribute(anyString());

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                String key = (String) invocationOnMock.getArguments()[0];
                Object value = invocationOnMock.getArguments()[1];
                objectMap.put(key, value);
                return null;
            }
        }).when(mockHttpSession).setAttribute(anyString(), any());
    }


    //-----------Sjekk LoggInn--------------------
    //Tester sjekk loggInn_Ok

    @Test
    public void sjekkLoggInn_LoggetInn_OK() {

        //arrange
        when(bankRepository.sjekkLoggInn(anyString(), anyString())).thenReturn("OK");

        //act
        String result = sikkerhetsController.sjekkLoggInn("01019505423", "heiPer");

        //assert
        assertEquals("OK", result);
    }

    //tester sjekk logginn_Ikke_OK

    @Test
    public void sjekkLoggInn_Feil_i_PersonnummerEllerPassord() {
        //arrange
        when(bankRepository.sjekkLoggInn(anyString(), anyString()))
                .thenReturn("Feil i personnummer eller passord");

        //act

        String result = sikkerhetsController.sjekkLoggInn("01019150523", "heiheiPer");

        //assert

        assertEquals("Feil i personnummer eller passord", result);

    }

    // Tester sjekk LoggInn_IkkeLoggetInn_FeilMedRegexPersonnummer)
    @Test
    public void sjekkLoggInn_FeilMedRegexPersonnummer() {
        //-------------act----------------------------------
        // feil med Personnummer-kort
        String result1 = sikkerhetsController.sjekkLoggInn("11223344",
                "HeiPer");

        // feil med personnummer-langt
        String result2 = sikkerhetsController.sjekkLoggInn("123456789987654321",
                "HeiPer");

        // personnummer som består av tegn og symboker
        String result3 = sikkerhetsController.sjekkLoggInn("//%%~~~@£$€%¤#",
                "HeiPer");

        // Bokstaver som Personnummer
        String result4 = sikkerhetsController.sjekkLoggInn("personnr",
                "HeiPer");

        // assert
        assertEquals("Feil i personnummer", result1);
        assertEquals("Feil i personnummer", result2);
        assertEquals("Feil i personnummer", result3);
        assertEquals("Feil i personnummer", result4);
    }

    // Tester sjekk LoggInn -IkkeLoggetInn - Feil Med Regex Passord)
    @Test
    public void sjekkLoggInn_FeilMedRegexPassord() {
        // act
        // ingen Passord
        String result1 = sikkerhetsController.sjekkLoggInn("01019150523", "");

        // Passord--langt
        String result2 = sikkerhetsController.
                sjekkLoggInn("01019150523", "HeiHeiHeiPer012345678909876543210");

        // Passord--kort
        String result3 = sikkerhetsController.sjekkLoggInn("01019150523", "hei");

        // assert
        assertEquals("Feil i passord", result1);
        assertEquals("Feil i passord", result2);
        assertEquals("Feil i passord", result3);
    }

    //--------------loggUt------------------------------
    //tester logg ut

    @Test
    public void LoggUt() {
        //arrange
        mockHttpSession.setAttribute("Innlogget", null);
        sikkerhetsController.loggUt();

        //act
        String resultat = sikkerhetsController.loggetInn();

        //assert
        assertNull(resultat);
    }


    //-------------------loggInn -Admin---------------------
    //tester loggInn_LoggInn_OK

    @Test
    public void adminLoggInn_OK() {

        //arrange
        mockHttpSession.setAttribute("Innlogget", null);

        //act
        String result = sikkerhetsController.loggInnAdmin("Admin", "Admin");

         //assert
        assertEquals("Logget inn", result);
    }


    //------logg inn-------
    //tester loggInn_loggInn_OK

    @Test
    public void loggInn_LoggetInn_OK(){
        //arrange
        mockHttpSession.setAttribute("Innlogget","01019150523");

        //act
        String result =sikkerhetsController.loggetInn();

        //assert
        assertEquals("01019150523",result);

    }

    //tester loggInn- ikkeLoggetInn

    @Test
    public void LoggetInn_IkkeLoggetInn(){
        // arrange
        mockHttpSession.setAttribute(null, null);

        // act
        String resultat = sikkerhetsController.loggetInn();

        // assert
        assertNull(resultat);
    }


}
