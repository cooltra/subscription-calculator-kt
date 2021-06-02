# Dev recruitment pair test

There is a pair test to perform with the candidate.

* You need two internal devs, one being a BA and the other being a dev
* Allow an hour

## Problem Summary

The accounts department are worried that we are paying claimants incorrect benefits.

## Business Rules

El solicitante de la ayuda declara cuánto paga de alquiler.
El alquiler se puede pagar quincenal o mensualmente.
La ayuda se recibe (siempre) mensualmente.
La ayuda recibida es el menor de:

    - el alquiler que paga el solicitante, 
    - el importe basado en el número de habitaciones de la vivienda (cap):

| Rooms        | Cap           |
| ------------- |:-------------:|
| 1      | 250 |
| 2      | 400      |
| >2     | 600      |

Cálculo impuesto para quincenas:

    - RENTA_QUINCENAL * QUINCENAS_POR_MES
    - QUINCENAS_POR_MES = (SEMANAS_AÑO / 2)/MESES_AÑO
    - (se hace un calculo medio de todos los meses)
    - QUINCENAS_POR_MES = (52/2) / 12 = 2.16666

Ejemplo:

    - cálculo = 175 * (52/2) / 12  = 379.166666667
    - y el round-up de 379.166666667 es 380

## Bugs

* Paying when under cap (missing else block), forces candidate to inject in a stub. Our policy is to round up to the nearest pound
* Switch statement without break or return in method cap
* Integer arithmetic on calculation leads to truncation problem. Need to change method to return double and use ceil.

## Conversation Points

* If condition on enum rental frequency, what happens if we have more than 2 types in the future
    * throw exception
    * push calculation onto the enum itself.

* Static initialisation of the AwardServiceServer. We want to maintain the behaviour of the production code but be able to wire in our implementation for testing
    * constructor injection vs field setting.
    * see how they feel about spring holy wars.
    * Creating a stub vs using a mocking framework (in this test we'd prefer they'd roll their own, in practise what do they use and why?)

* Breaking code out into collaborators.
* What happens if the connect method of AwardServiceServer throws an exception?
* How to focus more in use cases testing to provide scenarios that make sense to business, and less in property-based testing

## Expected tests

The following are the minimum, ideally we'd want each combo for fortnightly and monthly with bedrooms 1,2,3,+ and above and below cap.

* 1 bedroom with £100 is under the cap, should return £100
* 1 bedroom with £300 should be capped to £250
* 2 bedroom with £500 should be capped to £400
* 3 bedroom with £700 should be capped to £600
* more than 3 bedrooms as above
* To force them to fix the rounding issue ask for a rental payment of £54 fortnightly which should give £117


| Rooms|Rent| fortnightly /monthly| RESULT | Comment |
| ------------- |:-------------:|:-------------:|:-------------:|:-------------:|
| 1   | 100 | monthly | **100** | Basic case   -> TEST|
| 1   | 300 | monthly | **250** | Basic case   -> TEST|
| 2   | 350 | monthly | **350** ||
| 2   | 500 | monthly | **400** ||
| >2  | 550 | monthly | **550** ||
| >2  | 700 | monthly | **600** ||
| 1   | 54  | fortnightly | **117** | No necessary round up|
| 1   | 175 | fortnightly | **250** ||
| 2   | 175 | fortnightly | **380** | Round up 379,16  -> TEST|
| 2   | 250 | fortnightly | **400** | |
| >2  | 256 | fortnightly | **555** | Round up 554,67|
| >2  | 300 | fortnightly | **600** ||
