package com.kotlinspring.assignment2kotlin.service

import com.kotlinspring.assignment2kotlin.model.ECommerce
import com.kotlinspring.assignment2kotlin.repository.ECommerceRepository
import mu.KLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import java.io.InputStream
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ECommerceService(val eCommerceRepository: ECommerceRepository) {

    companion object : KLogging()

    var dateFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy H:mm")

    fun getECommerceListFromCsv(inputStream: InputStream): List<ECommerce> =
            CSVFormat.Builder.create(CSVFormat.EXCEL).apply {
                setIgnoreSurroundingSpaces(true)
            }.build().parse(inputStream.reader())
                    .drop(1) // Dropping the header
                    .map {
                        ECommerce(
                                id = 0,
                                invoiceNo = it[0],
                                stockCode = it[1],
                                description = it[2],
                                quantity = (it[3]).toInt(),
                                invoiceDate = LocalDate.parse((it[4]), dateFormatter),
                                unitPrice =  it[5].toBigDecimal(),
                                customerId = (it[6]).toInt(),
                                country = (it[7])
                        )
                    }

    fun saveECommerceList(inputStream: InputStream){
        val eCommerceList = getECommerceListFromCsv(inputStream)
        eCommerceRepository.saveAll(eCommerceList)
    }
}