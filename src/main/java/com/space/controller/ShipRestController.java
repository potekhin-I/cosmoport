package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class ShipRestController {

    private ShipService service;

    @Autowired
    public void setShipService (ShipService service) {
        this.service = service;
    }

    @GetMapping(value = "/ships")
    public ResponseEntity<List<Ship>> getAllShips(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "planet", required = false) String planet,
                                                  @RequestParam(value = "shipType", required = false) ShipType shipType,
                                                  @RequestParam(value = "after", required = false) Long after,
                                                  @RequestParam(value = "before", required = false) Long before,
                                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                                  @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                  @RequestParam(value = "minRating", required = false) Double minRating,
                                                  @RequestParam(value = "maxRating", required = false) Double maxRating,
                                                  @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {


        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        List<Ship> sortList = service.gelAllShips(
                Specification.where(service.filterByName(name)
                        .and(service.filterByPlanet(planet)))
                        .and(service.filterByShipType(shipType))
                        .and(service.filterByDate(after, before))
                        .and(service.filterByUsage(isUsed))
                        .and(service.filterBySpeed(minSpeed, maxSpeed))
                        .and(service.filterByCrewSize(minCrewSize, maxCrewSize))
                        .and(service.filterByRating(minRating, maxRating)), pageable)
                .getContent();
        return new ResponseEntity<>(sortList, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public ResponseEntity<Integer> getCount(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "planet", required = false) String planet,
                            @RequestParam(value = "shipType", required = false) ShipType shipType,
                            @RequestParam(value = "after", required = false) Long after,
                            @RequestParam(value = "before", required = false) Long before,
                            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                            @RequestParam(value = "minRating", required = false) Double minRating,
                            @RequestParam(value = "maxRating", required = false) Double maxRating) {
        Integer sortListSize  = service.gelAllShips(
                Specification.where(service.filterByName(name)
                        .and(service.filterByPlanet(planet)))
                        .and(service.filterByShipType(shipType))
                        .and(service.filterByDate(after, before))
                        .and(service.filterByUsage(isUsed))
                        .and(service.filterBySpeed(minSpeed, maxSpeed))
                        .and(service.filterByCrewSize(minCrewSize, maxCrewSize))
                        .and(service.filterByRating(minRating, maxRating)))
                .size();
        return new ResponseEntity<>(sortListSize, HttpStatus.OK);
    }

    @PostMapping(value = "/ships")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Ship createShip(@RequestBody Ship ship) {

        return service.createShip(ship);

    }

    @GetMapping(value = "/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Ship getShip(@PathVariable(value = "id") String id) {

        Long longId = service.checkAndParseId(id);

        return service.getShip(longId);
    }

    @PostMapping(value = "/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Ship updateShip(@PathVariable(value = "id") String id, @RequestBody Ship ship) {

        Long longId = service.checkAndParseId(id);

        return service.editShip(longId, ship);
    }

    @DeleteMapping(value = "/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteShip(@PathVariable(value = "id") String id) {

        Long longId = service.checkAndParseId(id);

        service.deleteById(longId);

    }
}
