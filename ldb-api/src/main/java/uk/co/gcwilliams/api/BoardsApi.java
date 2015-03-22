package uk.co.gcwilliams.api;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import uk.co.gcwilliams.codes.StationCodes;
import uk.co.gcwilliams.ldb.model.Id;
import uk.co.gcwilliams.ldb.model.Service;
import uk.co.gcwilliams.ldb.model.ServiceDetail;
import uk.co.gcwilliams.ldb.model.StationBoard;
import uk.co.gcwilliams.ldb.model.StationCode;
import uk.co.gcwilliams.ldb.service.StationBoards;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

/**
 * @author Gareth Williams (466567)
 */
@RolesAllowed("ldb-api")
@Path("/boards")
public class BoardsApi {

    private final StationCodes codes;

    private final StationBoards boards;

    /**
     * Default constructor
     *
     * @param codes The station codes
     * @param boards The station boards
     */
    @Inject
    public BoardsApi(StationCodes codes, StationBoards boards) {
        this.codes = codes;
        this.boards = boards;
    }

    @GET
    @Path("/departures/{stationCode}")
    public StationBoard getDeparturesBoard(@PathParam("stationCode") String stationCode, @QueryParam("to") String to) {

        Optional<StationCode> code = codes.getCode(stationCode);

        if (!code.isPresent()) {
            throw new WebApplicationException(400);
        }

        if (Strings.isNullOrEmpty(to)) {
            return boards.getDepartureBoard(code.get());
        }

        Optional<StationCode> toCode = codes.getCode(to);
        if (!toCode.isPresent()) {
            throw new WebApplicationException(400);
        }

        return boards.getDepartureBoard(code.get(), toCode.get());
    }

    @GET
    @Path("/arrivals/{stationCode}")
    public StationBoard getArrivalsBoard(@PathParam("stationCode") String stationCode, @QueryParam("from") String from) {

        Optional<StationCode> code = codes.getCode(stationCode);

        if (!code.isPresent()) {
            throw new WebApplicationException(400);
        }

        if (Strings.isNullOrEmpty(from)) {
            return boards.getArrivalBoard(code.get());
        }

        Optional<StationCode> fromCode = codes.getCode(from);
        if (!fromCode.isPresent()) {
            throw new WebApplicationException(400);
        }

        return boards.getArrivalBoard(code.get(), fromCode.get());
    }

    @GET
    @Path("/detail/{serviceId:.+}")
    public ServiceDetail getServiceDetail(@PathParam("serviceId") String serviceId) {
        return boards.getServiceDetail(new Id<Service>(serviceId));
    }
}
