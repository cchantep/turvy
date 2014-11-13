# Turvy

Little Scala (2.11) client to check VAT number (or some Economist at the Royal Bank of Ankh-Morpork)

Base on WSDL service exposed by the [European Commission](http://europa.eu/youreurope/business/vat-customs/check-number-vies/).

## Setup

In Maven project:

```xml
<project>
  <!-- ... -->
  
  <repository>
    <id>applicius-releases</id>
    <name>Applicius Releases</name>
    <url>https://raw.github.com/applicius/mvn-repo/master/releases/</url>
  </repository>

  <!-- ... -->

  <dependency>
    <groupId>fr.applicius.turvy</groupId>
    <artifactId>vat-client_2.11</artifactId>
    <version>1.0</version>
  </dependency>

  <dependency><!-- If mixing WS stub with dispatch impl -->
    <groupId>net.databinder.dispatch</groupId>
    <artifactId>dispatch-core_2.11</artifactId>
    <version>0.11.1</version>
  </dependency>

  <!-- ... -->
</project>
```

In SBT project:

```scala
resolvers ++= Seq(
  "Applicius Releases" at "https://raw.github.com/applicius/mvn-repo/master/releases/")

libraryDependencies ++= Seq(
  "fr.applicius.turvy" %% "vat-client" % "1.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.1"/* If mixing WS with
    displatch implementation */
```

## Usage

```scala
lazy val vatService = (new eu.europa.ec.CheckVatBindings with
    scalaxb.Soap11ClientsAsync with
    scalaxb.DispatchHttpClientsAsync {}).service
```

VAT service is providing following functions.

**checkVat**

Check a VAT number is registered for a european company.

```scala
def checkVat(countryCode: String, vatNumber: String): Future[eu.europa.ec.CheckVatResponse]
```

Deployed as REST at `https://turvy-demo.herokuapp.com/check/{country}/{num}`, with `{country}` to be replaced with a 2-letters country code (e.g. `FR`) and `{num}` with a company number. It returns a JSON response:

```json
// If VAT number is not registered
{"valid":false}

// If VAT number is registered
{"valid":true,"name":"COMPANY NAME","address":"COMPANY ADDRESS","requestDate":"0000-00-00Z"}
```

```javascript
// With jQuery
$.getJSON("https://turvy-demo.herokuapp.com/check/"+country+"/"+number,function(r){...})
```

[Try it online](https://turvy-demo.herokuapp.com#checkVatNumber)

**checkVatApprox**

```scala
def checkVatApprox(countryCode: String, vatNumber: String, traderName: Option[String], traderCompanyType: Option[String], traderStreet: Option[String], traderPostcode: Option[String], traderCity: Option[String], requesterCountryCode: Option[String], requesterVatNumber: Option[String]): Future[eu.europa.ec.CheckVatApproxResponse]
```

Data types `CheckVatResponse` and `CheckVatApproxResponse` are defined as following in `eu.europa.ec` namespace/package.

```scala
case class CheckVatResponse(countryCode: String,
  vatNumber: String,
  requestDate: javax.xml.datatype.XMLGregorianCalendar,
  valid: Boolean,
  name: Option[Option[String]],
  address: Option[Option[String]])

case class CheckVatApprox(countryCode: String,
  vatNumber: String,
  traderName: Option[String],
  traderCompanyType: Option[String],
  traderStreet: Option[String],
  traderPostcode: Option[String],
  traderCity: Option[String],
  requesterCountryCode: Option[String],
  requesterVatNumber: Option[String])
```

## Build

Built using SBT: `sbt publish-local`
