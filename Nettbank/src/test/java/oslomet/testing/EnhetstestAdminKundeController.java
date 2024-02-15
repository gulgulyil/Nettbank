package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKundeController {
    @InjectMocks
    //denne skal testes
    private AdminKundeController adminKundeController;

    @Mock//denne skal Mock'es
    private AdminRepository repository;

    @Mock
    //dene skal Mock'es
    private Sikkerhet sjekk;

    // Hent alle kunder
    //Tester hent alle kunder (Logget_inn-OK)
    @Test
    public void hentAlle_LoggetInn_OK(){
    //arrange
    List<Kunde> kundeList =new ArrayList<>();

    Kunde kunde1= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","Oslo","22224444","HeiHei");
    Kunde kunde2=new Kunde("12345678901","Per","Hansen","Osloveien 82","1234","Drammen","12345678","HeiHei");

    kundeList.add(kunde1);
    kundeList.add(kunde2);

    when(sjekk.loggetInn()).thenReturn("01010110523");
    when(repository.hentAlleKunder()).thenReturn(kundeList);

    //act
    List<Kunde> resultat=adminKundeController.hentAlle();

    //assert
    assertEquals(kundeList,resultat);
    }

    //Tester hent alle kunder (Ikke Logget Inn)

    @Test
    public void hentAlle_IkkeLoggetInn(){
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        List<Kunde> resultat = adminKundeController.hentAlle();

        //assert
        assertNull(resultat);
    }

    //Lagre Kunde
    //Tester lagre kunde (Logget Inn-OK)
    @Test
    public void lagreKunde_LoggetInn_OK(){
        //arrange
        Kunde kunde1= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","Oslo","22224444","HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat= adminKundeController.lagreKunde(kunde1);

        //assert
        assertEquals("OK",resultat);
    }

    //Tester lagre kunde(Ikke LoggetInn)
    @Test
    public void lagreKunde_IkkeLoggetInn(){
        //arrange
        Kunde kunde1=new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","Oslo","22224444","HeiHei");

        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat=adminKundeController.lagreKunde(kunde1);

        //assert
        assertEquals("Ikke logget inn",resultat);
    }

    //Endre Kunde
    //Tester endre kunde(Logget Inn_OK)
    @Test
    public void endreKunde_LoggetInn_OK(){
        //arrange
        Kunde kunde1=new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","Oslo","22224444","HeiHei");

        when(sjekk.loggetInn()).thenReturn("02020220523");
        when(repository.endreKundeInfo(kunde1)).thenReturn("OK");

        //act
        String resultat=adminKundeController.endre(kunde1);

        //assert
        assertEquals("OK",resultat);
    }

    //Tester endre kunde(Ikke Logget Inn)
    @Test
    public void endreKunde_IkkeLoggetInn(){
        //arrange
        Kunde kunde1=new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","Oslo","22224444","HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat=adminKundeController.endre(kunde1);

        //assert
        assertEquals("Ikke logget inn",resultat);
    }

    //Slett kunde
    //Tester slett kunde(Logget Inn-OK)

    @Test
    public void slettKunde_LoggetInn_OK(){
        //arrange
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.slettKunde(anyString())).thenReturn("OK");

        //act
        String resultat=adminKundeController.slett("01010110523");

        //assert
        assertEquals("OK",resultat);
    }

    //Tester slett kunde (Ikke Logget Inn)
    @Test
    public void slettKunde_IkkeLoggetInn(){
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat=adminKundeController.slett("01010110523");

        //assert
        assertEquals("Ikke logget inn",resultat);
    }

}
