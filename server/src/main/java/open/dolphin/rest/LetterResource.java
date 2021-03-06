package open.dolphin.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import open.dolphin.converter.LetterModuleConverter;
import open.dolphin.converter.LetterModuleListConverter;
import open.dolphin.infomodel.LetterModule;
import open.dolphin.infomodel.LetterModuleList;
import open.dolphin.session.LetterServiceBean;

/**
 * REST Web Service
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Path("/odletter")
public class LetterResource extends AbstractResource {

    @Inject
    private LetterServiceBean letterServiceBean;

    /** Creates a new instance of KarteResource */
    public LetterResource() {
    }

    @PUT
    @Path("/letter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String putLetter(String json) throws IOException {
        
        ObjectMapper mapper = new ObjectMapper();
        // 2013/06/24
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LetterModule model = mapper.readValue(json, LetterModule.class);
        
        Logger.getLogger("open.dolphin").log(Level.INFO, "LinkID : {0}, PatID : {1}", new Object[]{String.valueOf(model.getLinkId()), model.getPatientId()});

        Long pk = letterServiceBean.saveOrUpdateLetter(model);

        String pkStr = String.valueOf(pk);
        debug(pkStr);

        return pkStr;
    }

    @GET
    @Path("/list/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public LetterModuleListConverter getLetterList(@PathParam("param") String param) {

        debug(param);
        String[] params = param.split(CAMMA);
        long karteId = Long.parseLong(params[0]);

        List<LetterModule> result = letterServiceBean.getLetterList(karteId);
        LetterModuleList list = new LetterModuleList();
        if (result!=null && result.size()>0) {
            list.setList(result);
        } else {
            list.setList(new ArrayList<LetterModule>());
        }
        
        LetterModuleListConverter conv = new LetterModuleListConverter();
        conv.setModel(list);

        return conv;
    }

    @GET
    @Path("/letter/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public LetterModuleConverter getLetter(@PathParam("param") String param) {

        long pk = Long.parseLong(param);

        LetterModule result = (LetterModule)letterServiceBean.getLetter(pk);
        
        LetterModuleConverter conv = new LetterModuleConverter();
        conv.setModel(result);

        return conv;
    }

    @DELETE
    @Path("/letter/{param}")
    public void delete(@PathParam("param") String param) {

        long pk = Long.parseLong(param);
        letterServiceBean.delete(pk);
    }
}