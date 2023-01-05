rootProject.name = "Server"
include("brightnessService")
include("common")
include("greenhouseGteway")
include("brightnessService:greenhouseComunicationGateway")
findProject(":brightnessService:greenhouseComunicationGateway")?.name = "greenhouseComunicationGateway"
include("greenhouseComunicationGateway")
include("greenhouseCommunicationGateway")
