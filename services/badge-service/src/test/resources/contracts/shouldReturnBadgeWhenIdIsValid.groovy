//package contracts
//
//import org.springframework.cloud.contract.spec.Contract
//
//Contract.make {
//    description("Should return badge when id is valid")
//    request {
//        method GET()
//        url("/627e12175245286eadd55b70")
//    }
//    response {
//        body(file("getBadgeByIdResponse.json"))
//        status(200)
//        headers {
//            contentType(applicationJson())
//        }
//    }
//}
