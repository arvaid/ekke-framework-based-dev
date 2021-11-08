package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.SaleDto;
import hu.uni.eku.tzs.controller.dto.SaleMapper;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.SaleManager;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "Sales")
@RequestMapping("/sales")
@RestController
@RequiredArgsConstructor
public class SaleController {

    private final SaleManager saleManager;

    private final SaleMapper saleMapper;

    @ApiOperation("Read All")
    @GetMapping("/")
    public Collection<SaleDto> readAllSales() {
        return saleManager.readAll()
                .stream()
                .map(saleMapper::sale2saleDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("Record")
    @PostMapping("/")
    public SaleDto create(@RequestBody SaleDto saleDto) {
        Sale sale = saleMapper.saleDto2sale(saleDto);
        try {
            Sale recordedSale = saleManager.record(sale);
            return saleMapper.sale2saleDto(recordedSale);
        } catch (SaleAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"", "/"})
    public void delete(@RequestParam int id) {
        try {
            saleManager.delete(saleManager.readById(id));
        } catch (SaleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"/{id}"})
    public void deleteBasedOnPath(@PathVariable int id) {
        this.delete(id);
    }
}
