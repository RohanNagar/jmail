| Address | Expected | JMail | Apache Commons | Javax Mail |
| --- | :---: | :---: | :---: | :---: |
| "qu@test.org</br> | Invalid | ✅</br>in 8050.93 ns | ✅</br>in 20886.89 ns | ✅</br>in 28726.15 ns |
| ote"@test.org</br> | Invalid | ✅</br>in 2337.95 ns | ✅</br>in 24715.58 ns | ✅</br>in 24302.13 ns |
| "(),:;<>[\]@example.com</br> | Invalid | ✅</br>in 11196.5 ns | ✅</br>in 17540.84 ns | ✅</br>in 15807.88 ns |
| """@iana.org</br> | Invalid | ✅</br>in 4906.43 ns | ✅</br>in 8530.66 ns | ✅</br>in 26150.52 ns |
| Abc.example.com</br> | Invalid | ✅</br>in 7652.34 ns | ✅</br>in 2680.34 ns | ✅</br>in 64709.14 ns |
| A@b@c@example.com</br> | Invalid | ✅</br>in 7123.44 ns | ✅</br>in 6469.09 ns | ✅</br>in 45133.47 ns |
| a"b(c)d,e:f;g<h>i[j\k]l@example.com</br> | Invalid | ✅</br>in 2150.18 ns | ✅</br>in 41754.1 ns | ✅</br>in 33361.53 ns |
| just"not"right@example.com</br> | Invalid | ✅</br>in 6287.78 ns | ✅</br>in 36695.29 ns | ✅</br>in 49348.93 ns |
| this is"not\allowed@example.com</br> | Invalid | ✅</br>in 2122.68 ns | ✅</br>in 18360.02 ns | ✅</br>in 33218.27 ns |
| this\ still\"not\\allowed@example.com</br> | Invalid | ✅</br>in 2559.34 ns | ❌</br>in 44628.6 ns | ✅</br>in 24001.55 ns |
| 1234567890123456789012345678901234567890</br>123456789012345678901234+x@example.com</br> | Invalid | ✅</br>in 14660.7 ns | ✅</br>in 4652.91 ns | ❌</br>in 42597.04 ns |
| i_like_underscore@but_its_not_allowed_in</br>_this_part.example.com</br> | Invalid | ✅</br>in 96230.45 ns | ✅</br>in 138140.47 ns | ✅</br>in 26780.64 ns |
| QA[icon]CHOCOLATE[icon]@test.com</br> | Invalid | ✅</br>in 21893.7 ns | ✅</br>in 6489.07 ns | ✅</br>in 24884.02 ns |
| plainaddress</br> | Invalid | ✅</br>in 2433.53 ns | ✅</br>in 2767.89 ns | ✅</br>in 37726.59 ns |
| @example.com</br> | Invalid | ✅</br>in 2866.6 ns | ✅</br>in 2693.93 ns | ✅</br>in 38759.41 ns |
| @NotAnEmail</br> | Invalid | ✅</br>in 2253.21 ns | ✅</br>in 1060.37 ns | ✅</br>in 24042.52 ns |
| Joe Smith <email@example.com></br> | Invalid | ✅</br>in 1402.33 ns | ✅</br>in 44497.46 ns | ❌</br>in 4204.65 ns |
| email.example.com</br> | Invalid | ✅</br>in 4296.27 ns | ✅</br>in 1578.69 ns | ✅</br>in 192601.76 ns |
| email@example@example.com</br> | Invalid | ✅</br>in 3162.25 ns | ✅</br>in 12804.51 ns | ✅</br>in 108662.81 ns |
| .email@example.com</br> | Invalid | ✅</br>in 210.37 ns | ✅</br>in 7080.16 ns | ✅</br>in 48921.73 ns |
| email.@example.com</br> | Invalid | ✅</br>in 3357.42 ns | ✅</br>in 4990.91 ns | ✅</br>in 34606.48 ns |
| email..email@example.com</br> | Invalid | ✅</br>in 8297.41 ns | ✅</br>in 5296.3 ns | ✅</br>in 35613.66 ns |
| email@-example.com</br> | Invalid | ✅</br>in 3806.31 ns | ✅</br>in 37619.72 ns | ❌</br>in 7226.41 ns |
| email@111.222.333.44444</br> | Invalid | ✅</br>in 25091.24 ns | ✅</br>in 13457.87 ns | ❌</br>in 3675.35 ns |
| email@example..com</br> | Invalid | ✅</br>in 19489.66 ns | ✅</br>in 17796.89 ns | ✅</br>in 55635.8 ns |
| Abc..123@example.com</br> | Invalid | ✅</br>in 1301.65 ns | ✅</br>in 3896.62 ns | ✅</br>in 27685.17 ns |
| just"not"right@example.com</br> | Invalid | ✅</br>in 1325.47 ns | ✅</br>in 3964.08 ns | ✅</br>in 24030.48 ns |
| this\ is"really"not\allowed@example.com</br> | Invalid | ✅</br>in 1663.53 ns | ✅</br>in 5158.86 ns | ✅</br>in 24490.66 ns |
| first.last@sub.do,com</br> | Invalid | ✅</br>in 26237.53 ns | ✅</br>in 31766.76 ns | ✅</br>in 24393.28 ns |
| first\@last@iana.org</br> | Invalid | ✅</br>in 1759.17 ns | ❌</br>in 7659.1 ns | ❌</br>in 3420.93 ns |
| email@[12.34.44.56</br> | Invalid | ✅</br>in 2087.26 ns | ✅</br>in 8296.54 ns | ✅</br>in 42576.72 ns |
| email@14.44.56.34]</br> | Invalid | ✅</br>in 29238.71 ns | ✅</br>in 29136.23 ns | ❌</br>in 2506.8 ns |
| email@[1.1.23.5f]</br> | Invalid | ✅</br>in 30677.81 ns | ✅</br>in 9735.45 ns | ❌</br>in 2257.13 ns |
| email@[3.256.255.23]</br> | Invalid | ✅</br>in 3408.84 ns | ✅</br>in 18041.65 ns | ❌</br>in 2140.73 ns |
| first.last</br> | Invalid | ✅</br>in 1963.54 ns | ✅</br>in 970.99 ns | ✅</br>in 19968.3 ns |
| 1234567890123456789012345678901234567890</br>1234567890123456789012345@test.org</br> | Invalid | ✅</br>in 11894.1 ns | ✅</br>in 4380.35 ns | ❌</br>in 10391.87 ns |
| .first.last@test.org</br> | Invalid | ✅</br>in 175.25 ns | ✅</br>in 2957.31 ns | ✅</br>in 23588.41 ns |
| first.last.@test.org</br> | Invalid | ✅</br>in 4279.43 ns | ✅</br>in 16563.01 ns | ✅</br>in 88046.2 ns |
| first..last@test.org</br> | Invalid | ✅</br>in 1997.17 ns | ✅</br>in 4552.5 ns | ✅</br>in 32578.96 ns |
| "first"last"@test.org</br> | Invalid | ✅</br>in 2726.49 ns | ✅</br>in 3121.08 ns | ✅</br>in 31111.55 ns |
| first.last@[.12.34.56.78]</br> | Invalid | ✅</br>in 188039.42 ns | ✅</br>in 16074.31 ns | ❌</br>in 8750.3 ns |
| first.last@[12.34.56.789]</br> | Invalid | ✅</br>in 3987.74 ns | ✅</br>in 12084.07 ns | ❌</br>in 8078.2 ns |
| first.last@[::12.34.56.78]</br> | Invalid | ✅</br>in 2256.43 ns | ❌</br>in 19954.96 ns | ❌</br>in 6865.47 ns |
| x@x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456</br> | Invalid | ✅</br>in 46274.44 ns | ✅</br>in 32871.1 ns | ❌</br>in 13275.9 ns |
| "\"@iana.org</br> | Invalid | ✅</br>in 2584.82 ns | ❌</br>in 5729.05 ns | ✅</br>in 53469.73 ns |
| first\\@last@iana.org</br> | Invalid | ✅</br>in 1319.96 ns | ✅</br>in 3060.89 ns | ❌</br>in 6705.46 ns |
| first.last@</br> | Invalid | ✅</br>in 6757.55 ns | ✅</br>in 985.05 ns | ✅</br>in 42300.59 ns |
| test@example.com&#10;</br> | Invalid | ✅</br>in 32740.57 ns | ✅</br>in 6604.25 ns | ✅</br>in 21052.06 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777]</br> | Invalid | ✅</br>in 16003.96 ns | ✅</br>in 46229.35 ns | ❌</br>in 3137.0 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777:8888:9999]</br> | Invalid | ✅</br>in 15635.11 ns | ✅</br>in 18439.93 ns | ❌</br>in 2361.38 ns |
| first.last@[IPv6:1111:2222::3333::4444:5</br>555:6666]</br> | Invalid | ✅</br>in 9467.8 ns | ✅</br>in 20970.46 ns | ❌</br>in 3593.22 ns |
| first.last@[IPv6:1111:2222:333x::4444:55</br>55]</br> | Invalid | ✅</br>in 9157.5 ns | ✅</br>in 82447.64 ns | ❌</br>in 1939.84 ns |
| first.last@[IPv6:1111:2222:33333::4444:5</br>555]</br> | Invalid | ✅</br>in 4334.29 ns | ✅</br>in 49767.1 ns | ❌</br>in 2387.9 ns |
| first.last@[IPv6::]</br> | Invalid | ✅</br>in 2560.97 ns | ✅</br>in 37956.12 ns | ❌</br>in 1115.51 ns |
| first.last@[IPv6::::]</br> | Invalid | ✅</br>in 2274.85 ns | ✅</br>in 4614.84 ns | ❌</br>in 809.86 ns |
| first.last@[IPv6::b4]</br> | Invalid | ✅</br>in 2491.49 ns | ✅</br>in 31490.6 ns | ❌</br>in 2273.84 ns |
| first.last@[IPv6::::b4]</br> | Invalid | ✅</br>in 2643.04 ns | ✅</br>in 4852.72 ns | ❌</br>in 3892.65 ns |
| first.last@[IPv6::b3:b4]</br> | Invalid | ✅</br>in 2467.81 ns | ✅</br>in 57209.6 ns | ❌</br>in 1309.43 ns |
| first.last@[IPv6::::b3:b4]</br> | Invalid | ✅</br>in 2816.48 ns | ✅</br>in 9439.5 ns | ❌</br>in 2984.33 ns |
| first.last@[IPv6:a1:::b4]</br> | Invalid | ✅</br>in 2933.19 ns | ✅</br>in 4993.03 ns | ❌</br>in 1292.95 ns |
| first.last@[IPv6:a1:]</br> | Invalid | ✅</br>in 2154.88 ns | ✅</br>in 4877.27 ns | ❌</br>in 1205.37 ns |
| first.last@[IPv6:a1:::]</br> | Invalid | ✅</br>in 2897.68 ns | ✅</br>in 6470.99 ns | ❌</br>in 1154.63 ns |
| first.last@[IPv6:a1:a2:]</br> | Invalid | ✅</br>in 2617.06 ns | ✅</br>in 5772.36 ns | ❌</br>in 1970.79 ns |
| first.last@[IPv6:a1:a2:::]</br> | Invalid | ✅</br>in 3994.21 ns | ✅</br>in 8664.11 ns | ❌</br>in 1322.07 ns |
| first.last@[IPv6:a1::b3:]</br> | Invalid | ✅</br>in 2783.77 ns | ✅</br>in 6089.36 ns | ❌</br>in 1118.93 ns |
| first.last@[IPv6::a2::b4]</br> | Invalid | ✅</br>in 2620.09 ns | ✅</br>in 6281.68 ns | ❌</br>in 2086.33 ns |
| first.last@[IPv6:a1:a2:a3:a4:b1:b2:b3:]</br> | Invalid | ✅</br>in 2964.32 ns | ✅</br>in 8195.69 ns | ❌</br>in 1976.7 ns |
| first.last@[IPv6::a2:a3:a4:b1:b2:b3:b4]</br> | Invalid | ✅</br>in 2736.85 ns | ✅</br>in 11509.24 ns | ❌</br>in 1640.98 ns |
| first.last@[IPv6:a1:a2:a3:a4::b1:b2:b3:b</br>4]</br> | Invalid | ✅</br>in 6004.3 ns | ✅</br>in 11655.35 ns | ❌</br>in 2746.18 ns |
| first.last@[IPv6::11.22.33.44]</br> | Invalid | ✅</br>in 2781.86 ns | ✅</br>in 33364.4 ns | ❌</br>in 1323.27 ns |
| first.last@[IPv6::::11.22.33.44]</br> | Invalid | ✅</br>in 2616.89 ns | ✅</br>in 6434.18 ns | ❌</br>in 1769.4 ns |
| first.last@[IPv6:a1:11.22.33.44]</br> | Invalid | ✅</br>in 4696.72 ns | ✅</br>in 30382.21 ns | ❌</br>in 1359.98 ns |
| first.last@[IPv6:a1:::11.22.33.44]</br> | Invalid | ✅</br>in 2630.01 ns | ✅</br>in 6217.95 ns | ❌</br>in 1522.44 ns |
| first.last@[IPv6:a1:a2:::11.22.33.44]</br> | Invalid | ✅</br>in 2923.18 ns | ✅</br>in 6721.34 ns | ❌</br>in 1547.38 ns |
| first.last@[IPv6:0123:4567:89ab:cdef::11</br>.22.33.xx]</br> | Invalid | ✅</br>in 5276.02 ns | ✅</br>in 36722.86 ns | ❌</br>in 2787.95 ns |
| first.last@[IPv5:::12.34.56.78]</br> | Invalid | ✅</br>in 2117.29 ns | ✅</br>in 5599.99 ns | ❌</br>in 1459.33 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:12.34.56.78]</br> | Invalid | ✅</br>in 7147.95 ns | ✅</br>in 36797.56 ns | ❌</br>in 2571.25 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777:12.34.56.78]</br> | Invalid | ✅</br>in 5833.11 ns | ✅</br>in 7095.35 ns | ❌</br>in 1275.17 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:12.34.567.89]</br> | Invalid | ✅</br>in 5269.92 ns | ✅</br>in 26603.97 ns | ❌</br>in 1631.96 ns |
| aaa@[123.123.123.123]a</br> | Invalid | ✅</br>in 990.04 ns | ✅</br>in 4815.3 ns | ✅</br>in 27472.76 ns |
| aaa@[123.123.123.333]</br> | Invalid | ✅</br>in 2745.72 ns | ✅</br>in 8074.2 ns | ❌</br>in 1599.02 ns |
| first.last@[IPv6:0123:4567:89ab:CDEFF::1</br>1.22.33.44]</br> | Invalid | ✅</br>in 4274.5 ns | ✅</br>in 43450.16 ns | ❌</br>in 10370.67 ns |
| first.last@[IPv6:a1::a4:b1::b4:11.22.33.</br>44]</br> | Invalid | ✅</br>in 3787.58 ns | ✅</br>in 8662.33 ns | ❌</br>in 3922.38 ns |
| first.last@[IPv6:a1::11.22.33]</br> | Invalid | ✅</br>in 3847.84 ns | ✅</br>in 26856.95 ns | ❌</br>in 1736.72 ns |
| first.last@[IPv6:a1::11.22.33.44.55]</br> | Invalid | ✅</br>in 5171.74 ns | ✅</br>in 38602.26 ns | ❌</br>in 3327.48 ns |
| first.last@[IPv6:a1::b211.22.33.44]</br> | Invalid | ✅</br>in 4135.64 ns | ✅</br>in 40248.07 ns | ❌</br>in 2981.84 ns |
| first.last@[IPv6:a1::b2::11.22.33.44]</br> | Invalid | ✅</br>in 2730.22 ns | ✅</br>in 6606.76 ns | ❌</br>in 3342.78 ns |
| abc\@def@test.org</br> | Invalid | ✅</br>in 874.42 ns | ❌</br>in 5077.24 ns | ❌</br>in 2334.36 ns |
| a@-b.com</br> | Invalid | ✅</br>in 789.31 ns | ✅</br>in 2632.39 ns | ❌</br>in 1513.49 ns |
| a@b-.com</br> | Invalid | ✅</br>in 859.21 ns | ✅</br>in 7914.65 ns | ❌</br>in 1285.54 ns |
| -@..com</br> | Invalid | ✅</br>in 559.01 ns | ✅</br>in 1804.73 ns | ✅</br>in 23639.33 ns |
| -@a..com</br> | Invalid | ✅</br>in 915.06 ns | ✅</br>in 7157.97 ns | ✅</br>in 21363.65 ns |
| invalid@about.museum-</br> | Invalid | ✅</br>in 138.16 ns | ✅</br>in 11681.12 ns | ❌</br>in 1780.41 ns |
| test@...........com</br> | Invalid | ✅</br>in 967.43 ns | ✅</br>in 7132.2 ns | ✅</br>in 28731.01 ns |
| first.last@-xample.com</br> | Invalid | ✅</br>in 3919.15 ns | ✅</br>in 10715.32 ns | ❌</br>in 3411.88 ns |
| first.last@exampl-.com</br> | Invalid | ✅</br>in 3413.58 ns | ✅</br>in 12617.67 ns | ❌</br>in 4483.59 ns |
| first.last@x2345678901234567890123456789</br>01234567890123456789012345678901234.test</br>.org</br> | Invalid | ✅</br>in 9393.7 ns | ✅</br>in 53248.73 ns | ❌</br>in 11915.31 ns |
| abc\\@test.org</br> | Invalid | ✅</br>in 575.93 ns | ❌</br>in 7356.93 ns | ✅</br>in 15894.98 ns |
| abc@def@test.org</br> | Invalid | ✅</br>in 1347.93 ns | ✅</br>in 4715.36 ns | ✅</br>in 23051.4 ns |
| abc\\@def@test.org</br> | Invalid | ✅</br>in 1134.21 ns | ✅</br>in 5370.25 ns | ❌</br>in 2256.84 ns |
| abc\@test.org</br> | Invalid | ✅</br>in 967.11 ns | ✅</br>in 12681.52 ns | ✅</br>in 27222.62 ns |
| @test.org</br> | Invalid | ✅</br>in 1576.34 ns | ✅</br>in 2169.15 ns | ✅</br>in 23444.33 ns |
| doug@</br> | Invalid | ✅</br>in 1015.65 ns | ✅</br>in 1440.14 ns | ✅</br>in 21381.89 ns |
| .dot@test.org</br> | Invalid | ✅</br>in 90.99 ns | ✅</br>in 3757.65 ns | ✅</br>in 13615.97 ns |
| dot.@test.org</br> | Invalid | ✅</br>in 2551.83 ns | ✅</br>in 5656.87 ns | ✅</br>in 23327.86 ns |
| two..dot@test.org</br> | Invalid | ✅</br>in 1087.32 ns | ✅</br>in 3295.88 ns | ✅</br>in 18285.73 ns |
| "Doug "Ace" L."@test.org</br> | Invalid | ✅</br>in 1514.91 ns | ✅</br>in 5651.22 ns | ✅</br>in 21928.55 ns |
| Doug\ \"Ace\"\ L\.@test.org</br> | Invalid | ✅</br>in 975.2 ns | ❌</br>in 10383.66 ns | ✅</br>in 19908.17 ns |
| hello world@test.org</br> | Invalid | ✅</br>in 1431.7 ns | ✅</br>in 6899.48 ns | ✅</br>in 23906.14 ns |
| gatsby@f.sc.ot.t.f.i.tzg.era.l.d.</br> | Invalid | ✅</br>in 132.4 ns | ✅</br>in 96.64 ns | ✅</br>in 23869.92 ns |
| .@</br> | Invalid | ✅</br>in 81.86 ns | ✅</br>in 765.92 ns | ✅</br>in 23035.71 ns |
| @bar.com</br> | Invalid | ✅</br>in 1356.06 ns | ✅</br>in 2181.79 ns | ✅</br>in 20813.06 ns |
| @@bar.com</br> | Invalid | ✅</br>in 313.08 ns | ✅</br>in 3182.07 ns | ✅</br>in 21237.98 ns |
| aaa.com</br> | Invalid | ✅</br>in 1559.02 ns | ✅</br>in 1400.8 ns | ✅</br>in 18654.7 ns |
| aaa@.com</br> | Invalid | ✅</br>in 817.54 ns | ✅</br>in 2976.78 ns | ✅</br>in 22561.37 ns |
| aaa@.123</br> | Invalid | ✅</br>in 854.38 ns | ✅</br>in 2021.23 ns | ✅</br>in 21409.7 ns |
| a@bar.com.</br> | Invalid | ✅</br>in 106.78 ns | ✅</br>in 235.73 ns | ✅</br>in 30599.09 ns |
| {^c\@**Dog^}@cartoon.com</br> | Invalid | ✅</br>in 1488.44 ns | ❌</br>in 30251.12 ns | ❌</br>in 3379.58 ns |
| cal(foo(bar)@iamcal.com</br> | Invalid | ✅</br>in 6336.6 ns | ✅</br>in 2989.71 ns | ✅</br>in 25461.78 ns |
| cal(foo)bar)@iamcal.com</br> | Invalid | ✅</br>in 3718.77 ns | ✅</br>in 3397.36 ns | ✅</br>in 22932.89 ns |
| cal(foo\)@iamcal.com</br> | Invalid | ✅</br>in 2490.42 ns | ✅</br>in 2757.77 ns | ✅</br>in 22511.96 ns |
| first(1234567890123456789012345678901234</br>5678901234567890)last@(12345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>890123456789012345678901234567890)test.o</br>rg</br> | Invalid | ✅</br>in 90.03 ns | ✅</br>in 27950.01 ns | ✅</br>in 60840.74 ns |
| a(a(b(c)d(e(f))g)(h(i)j)@test.org</br> | Invalid | ✅</br>in 3854.0 ns | ✅</br>in 2290.67 ns | ✅</br>in 27555.66 ns |
| Doug\ \"Ace\"\ Lovell@test.org</br> | Invalid | ✅</br>in 1345.81 ns | ❌</br>in 7323.6 ns | ✅</br>in 26507.22 ns |
| test.test.org</br> | Invalid | ✅</br>in 2128.46 ns | ✅</br>in 798.59 ns | ✅</br>in 27811.69 ns |
| test.@test.org</br> | Invalid | ✅</br>in 3231.7 ns | ✅</br>in 2364.75 ns | ✅</br>in 21097.12 ns |
| test..test@test.org</br> | Invalid | ✅</br>in 954.28 ns | ✅</br>in 2437.36 ns | ✅</br>in 23196.7 ns |
| .test@test.org</br> | Invalid | ✅</br>in 99.96 ns | ✅</br>in 1456.11 ns | ✅</br>in 22817.27 ns |
| test@test@test.org</br> | Invalid | ✅</br>in 1888.76 ns | ✅</br>in 2362.28 ns | ✅</br>in 21339.97 ns |
| test@@test.org</br> | Invalid | ✅</br>in 1066.15 ns | ✅</br>in 2602.01 ns | ✅</br>in 20709.57 ns |
| -- test --@test.org</br> | Invalid | ✅</br>in 935.92 ns | ✅</br>in 2255.99 ns | ✅</br>in 24857.02 ns |
| [test]@test.org</br> | Invalid | ✅</br>in 414.84 ns | ✅</br>in 1539.86 ns | ✅</br>in 21755.56 ns |
| "test"test"@test.org</br> | Invalid | ✅</br>in 1390.47 ns | ✅</br>in 2242.85 ns | ✅</br>in 20100.68 ns |
| ()[]\;:,><@test.org</br> | Invalid | ✅</br>in 1028.56 ns | ✅</br>in 933.61 ns | ✅</br>in 17427.68 ns |
| test@.</br> | Invalid | ✅</br>in 112.32 ns | ✅</br>in 83.7 ns | ✅</br>in 20971.73 ns |
| test@example.</br> | Invalid | ✅</br>in 106.78 ns | ✅</br>in 68.36 ns | ✅</br>in 16618.66 ns |
| test@.org</br> | Invalid | ✅</br>in 1090.77 ns | ✅</br>in 2494.62 ns | ✅</br>in 16454.92 ns |
| test@12345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>67890123456789012.com</br> | Invalid | ✅</br>in 26731.14 ns | ✅</br>in 30666.88 ns | ❌</br>in 10407.71 ns |
| .wooly@test.org</br> | Invalid | ✅</br>in 93.16 ns | ✅</br>in 1270.45 ns | ✅</br>in 22410.68 ns |
| wo..oly@test.org</br> | Invalid | ✅</br>in 485.05 ns | ✅</br>in 1273.53 ns | ✅</br>in 23082.9 ns |
| pootieshoe.@test.org</br> | Invalid | ✅</br>in 2116.03 ns | ✅</br>in 2362.6 ns | ✅</br>in 14239.48 ns |
| .@test.org</br> | Invalid | ✅</br>in 90.18 ns | ✅</br>in 1168.91 ns | ✅</br>in 17735.3 ns |
| Ima Fool@test.org</br> | Invalid | ✅</br>in 1255.45 ns | ✅</br>in 1943.88 ns | ✅</br>in 21877.46 ns |
| phil.h\@\@ck@haacked.com</br> | Invalid | ✅</br>in 2446.69 ns | ❌</br>in 6121.13 ns | ❌</br>in 1267.98 ns |
| "first\\"last"@test.org</br> | Invalid | ✅</br>in 1642.38 ns | ❌</br>in 6068.66 ns | ✅</br>in 21566.1 ns |
| foo@[\1.2.3.4]</br> | Invalid | ✅</br>in 2416.79 ns | ✅</br>in 4687.27 ns | ❌</br>in 919.04 ns |
| first\last@test.org</br> | Invalid | ✅</br>in 1332.03 ns | ❌</br>in 6496.44 ns | ❌</br>in 1109.38 ns |
| Abc\@def@test.org</br> | Invalid | ✅</br>in 1108.47 ns | ❌</br>in 5745.16 ns | ❌</br>in 737.59 ns |
| Fred\ Bloggs@test.org</br> | Invalid | ✅</br>in 1411.37 ns | ❌</br>in 6673.19 ns | ❌</br>in 1417.5 ns |
| Joe.\\Blow@test.org</br> | Invalid | ✅</br>in 1134.55 ns | ❌</br>in 7261.95 ns | ❌</br>in 1251.25 ns |
| first(abc("def".ghi).mno)middle(abc("def</br>".ghi).mno).last@(abc("def".ghi).mno)exa</br>mple(abc("def".ghi).mno).(abc("def".ghi)</br>.mno)com(abc("def".ghi).mno)</br> | Invalid | ✅</br>in 6679.94 ns | ✅</br>in 14264.99 ns | ✅</br>in 19454.17 ns |
| first(middle)last@test.org</br> | Invalid | ✅</br>in 2883.42 ns | ✅</br>in 4046.68 ns | ✅</br>in 27659.87 ns |
| "Unicode NULL ␀"@char.com</br> | Invalid | ✅</br>in 3931.42 ns | ❌</br>in 9020.56 ns | ❌</br>in 9418.07 ns |
| Unicode NULL \␀@char.com</br> | Invalid | ✅</br>in 1540.05 ns | ✅</br>in 4237.27 ns | ✅</br>in 42040.41 ns |
| "test"test@test.com</br> | Invalid | ✅</br>in 1404.28 ns | ✅</br>in 4515.58 ns | ✅</br>in 23222.61 ns |
| first."".last@test.org</br> | Invalid | ✅</br>in 1656.35 ns | ❌</br>in 6789.64 ns | ✅</br>in 20126.18 ns |
| ""@test.org</br> | Invalid | ✅</br>in 608.83 ns | ❌</br>in 4089.83 ns | ❌</br>in 2058.38 ns |
| ()@test.com</br> | Invalid | ✅</br>in 2441.69 ns | ✅</br>in 1938.36 ns | ✅</br>in 17391.66 ns |
| " "@example.org</br> | Valid | ✅</br>in 15491.79 ns | ✅</br>in 4512.73 ns | ✅</br>in 2942.81 ns |
| "john..doe"@example.org</br> | Valid | ✅</br>in 11487.9 ns | ✅</br>in 6292.61 ns | ❌</br>in 25668.59 ns |
| "email"@example.com</br> | Valid | ✅</br>in 10790.49 ns | ✅</br>in 5264.73 ns | ✅</br>in 2127.8 ns |
| "first@last"@test.org</br> | Valid | ✅</br>in 11349.88 ns | ✅</br>in 6445.07 ns | ✅</br>in 1838.5 ns |
| very.unusual."@".unusual.com@example.com</br> | Valid | ✅</br>in 11043.98 ns | ✅</br>in 9569.12 ns | ❌</br>in 28473.67 ns |
| "first\"last"@test.org</br> | Valid | ✅</br>in 7322.77 ns | ✅</br>in 4973.73 ns | ✅</br>in 2559.46 ns |
| much."more\ unusual"@example.com</br> | Valid | ✅</br>in 9473.95 ns | ✅</br>in 6707.5 ns | ❌</br>in 19917.28 ns |
| "first\\last"@test.org</br> | Valid | ✅</br>in 7998.38 ns | ✅</br>in 4739.84 ns | ✅</br>in 2085.32 ns |
| "Abc\@def"@test.org</br> | Valid | ✅</br>in 7640.07 ns | ✅</br>in 5135.92 ns | ✅</br>in 2505.23 ns |
| "Fred\ Bloggs"@test.org</br> | Valid | ✅</br>in 4810.02 ns | ✅</br>in 3089.05 ns | ✅</br>in 1568.04 ns |
| "Joe.\\Blow"@test.org</br> | Valid | ✅</br>in 7821.01 ns | ✅</br>in 4903.8 ns | ✅</br>in 3066.48 ns |
| "Abc@def"@test.org</br> | Valid | ✅</br>in 6897.66 ns | ✅</br>in 4502.99 ns | ✅</br>in 2300.18 ns |
| "Fred Bloggs"@test.org</br> | Valid | ✅</br>in 7631.07 ns | ✅</br>in 5768.2 ns | ✅</br>in 3412.95 ns |
| "first\last"@test.org</br> | Valid | ✅</br>in 8652.67 ns | ✅</br>in 5348.86 ns | ✅</br>in 2621.12 ns |
| "Doug \"Ace\" L."@test.org</br> | Valid | ✅</br>in 10047.28 ns | ✅</br>in 4748.66 ns | ✅</br>in 2294.22 ns |
| "[[ test ]]"@test.org</br> | Valid | ✅</br>in 10408.46 ns | ✅</br>in 5215.58 ns | ✅</br>in 3517.0 ns |
| "test.test"@test.org</br> | Valid | ✅</br>in 7628.91 ns | ✅</br>in 5023.05 ns | ✅</br>in 2404.18 ns |
| test."test"@test.org</br> | Valid | ✅</br>in 7625.74 ns | ✅</br>in 5589.34 ns | ❌</br>in 16889.32 ns |
| "test@test"@test.org</br> | Valid | ✅</br>in 5272.93 ns | ✅</br>in 4448.85 ns | ✅</br>in 1527.57 ns |
| "test\test"@test.org</br> | Valid | ✅</br>in 8169.48 ns | ✅</br>in 4837.92 ns | ✅</br>in 2292.03 ns |
| "first"."last"@test.org</br> | Valid | ✅</br>in 7636.44 ns | ✅</br>in 4390.41 ns | ❌</br>in 22525.48 ns |
| "first".middle."last"@test.org</br> | Valid | ✅</br>in 8830.68 ns | ✅</br>in 7286.32 ns | ❌</br>in 43477.43 ns |
| "first".last@test.org</br> | Valid | ✅</br>in 7931.09 ns | ✅</br>in 5783.36 ns | ❌</br>in 23513.73 ns |
| first."last"@test.org</br> | Valid | ✅</br>in 6847.87 ns | ✅</br>in 5155.1 ns | ❌</br>in 160945.76 ns |
| "first"."middle"."last"@test.org</br> | Valid | ✅</br>in 8913.93 ns | ✅</br>in 8704.44 ns | ❌</br>in 28172.41 ns |
| "first.middle"."last"@test.org</br> | Valid | ✅</br>in 9781.62 ns | ✅</br>in 7997.98 ns | ❌</br>in 26028.08 ns |
| "first.middle.last"@test.org</br> | Valid | ✅</br>in 10742.82 ns | ✅</br>in 11577.85 ns | ✅</br>in 3171.09 ns |
| "first..last"@test.org</br> | Valid | ✅</br>in 7389.09 ns | ✅</br>in 4584.04 ns | ❌</br>in 29204.71 ns |
| "Unicode NULL \␀"@char.com</br> | Valid | ✅</br>in 9412.22 ns | ✅</br>in 5299.04 ns | ✅</br>in 2872.04 ns |
| "test\\blah"@test.org</br> | Valid | ✅</br>in 6595.86 ns | ✅</br>in 4103.22 ns | ✅</br>in 2065.03 ns |
| "test\blah"@test.org</br> | Valid | ✅</br>in 6996.31 ns | ✅</br>in 5635.34 ns | ✅</br>in 2126.02 ns |
| "test\"blah"@test.org</br> | Valid | ✅</br>in 7273.23 ns | ✅</br>in 4525.79 ns | ✅</br>in 2446.24 ns |
| "first\\\"last"@test.org</br> | Valid | ✅</br>in 7696.18 ns | ✅</br>in 4844.09 ns | ✅</br>in 3562.4 ns |
| first."mid\dle"."last"@test.org</br> | Valid | ✅</br>in 9252.71 ns | ✅</br>in 6824.57 ns | ❌</br>in 38674.91 ns |
| "Test \"Fail\" Ing"@test.org</br> | Valid | ✅</br>in 8998.31 ns | ✅</br>in 6661.41 ns | ✅</br>in 5142.6 ns |
| "test&#13;&#10; blah"@test.org</br> | Valid | ✅</br>in 8726.95 ns | ✅</br>in 8652.64 ns | ✅</br>in 4651.33 ns |
| first.last @test.org</br> | Valid | ✅</br>in 8193.76 ns | ❌</br>in 3673.64 ns | ❌</br>in 32723.65 ns |
| first.last  @test.org</br> | Valid | ✅</br>in 10282.79 ns | ❌</br>in 4684.47 ns | ❌</br>in 28175.52 ns |
| first .last  @test .org</br> | Valid | ✅</br>in 12015.58 ns | ❌</br>in 1757.79 ns | ❌</br>in 41349.98 ns |
| jdoe@machine(comment).  example</br> | Valid | ✅</br>in 12998.41 ns | ❌</br>in 3062.63 ns | ✅</br>in 2939.74 ns |
| simple@example.com</br> | Valid | ✅</br>in 12886.29 ns | ✅</br>in 8154.85 ns | ✅</br>in 4231.85 ns |
| very.common@example.com</br> | Valid | ✅</br>in 10740.44 ns | ✅</br>in 7425.27 ns | ✅</br>in 4767.01 ns |
| very.common@example.org</br> | Valid | ✅</br>in 10412.84 ns | ✅</br>in 7538.98 ns | ✅</br>in 4883.59 ns |
| disposable.style.email.with+symbol@examp</br>le.com</br> | Valid | ✅</br>in 12642.24 ns | ✅</br>in 11885.77 ns | ✅</br>in 10580.87 ns |
| other.email-with-hyphen@example.com</br> | Valid | ✅</br>in 16356.36 ns | ✅</br>in 11593.87 ns | ✅</br>in 7606.69 ns |
| fully-qualified-domain@example.com</br> | Valid | ✅</br>in 15012.88 ns | ✅</br>in 13234.54 ns | ✅</br>in 6175.45 ns |
| user.name+tag+sorting@example.com</br> | Valid | ✅</br>in 11790.95 ns | ✅</br>in 9662.56 ns | ✅</br>in 7839.12 ns |
| x@example.com</br> | Valid | ✅</br>in 9131.96 ns | ✅</br>in 4699.73 ns | ✅</br>in 2611.75 ns |
| example-indeed@strange-example.com</br> | Valid | ✅</br>in 20207.28 ns | ✅</br>in 11148.31 ns | ✅</br>in 8273.9 ns |
| test/test@test.com</br> | Valid | ✅</br>in 10125.85 ns | ✅</br>in 6144.29 ns | ✅</br>in 3652.93 ns |
| admin@mailserver1</br> | Valid | ✅</br>in 9406.38 ns | ✅</br>in 4528.48 ns | ✅</br>in 3092.46 ns |
| example@s.example</br> | Valid | ✅</br>in 16462.3 ns | ❌</br>in 7458.13 ns | ✅</br>in 6529.45 ns |
| mailhost!username@example.org</br> | Valid | ✅</br>in 11344.82 ns | ✅</br>in 16299.67 ns | ✅</br>in 6055.85 ns |
| user%example.com@example.org</br> | Valid | ✅</br>in 15298.47 ns | ✅</br>in 8140.77 ns | ✅</br>in 5723.96 ns |
| user-@example.org</br> | Valid | ✅</br>in 9365.91 ns | ✅</br>in 5139.32 ns | ✅</br>in 3750.86 ns |
| email@example.com</br> | Valid | ✅</br>in 11012.26 ns | ✅</br>in 8170.15 ns | ✅</br>in 4398.2 ns |
| firstname.lastname@example.com</br> | Valid | ✅</br>in 12857.69 ns | ✅</br>in 9658.59 ns | ✅</br>in 7358.57 ns |
| email@subdomain.example.com</br> | Valid | ✅</br>in 14819.21 ns | ✅</br>in 6821.57 ns | ✅</br>in 5904.02 ns |
| firstname+lastname@example.com</br> | Valid | ✅</br>in 13912.36 ns | ✅</br>in 9702.32 ns | ✅</br>in 9667.91 ns |
| 1234567890@example.com</br> | Valid | ✅</br>in 20926.57 ns | ✅</br>in 6989.23 ns | ✅</br>in 5698.89 ns |
| email@example-one.com</br> | Valid | ✅</br>in 12266.24 ns | ✅</br>in 25077.88 ns | ✅</br>in 4425.68 ns |
| _______@example.com</br> | Valid | ✅</br>in 9724.58 ns | ✅</br>in 5609.51 ns | ✅</br>in 3250.63 ns |
| email@example.name</br> | Valid | ✅</br>in 10061.06 ns | ✅</br>in 5535.62 ns | ✅</br>in 3384.02 ns |
| email@example.museum</br> | Valid | ✅</br>in 10515.18 ns | ✅</br>in 6751.76 ns | ✅</br>in 8720.01 ns |
| email@example.co.jp</br> | Valid | ✅</br>in 11991.52 ns | ✅</br>in 8399.51 ns | ✅</br>in 4098.77 ns |
| firstname-lastname@example.com</br> | Valid | ✅</br>in 13536.17 ns | ✅</br>in 15483.02 ns | ✅</br>in 7261.33 ns |
| x@x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x2</br> | Valid | ✅</br>in 99595.42 ns | ❌</br>in 50045.58 ns | ✅</br>in 34294.65 ns |
| 1234567890123456789012345678901234567890</br>123456789012345678@123456789012345678901</br>23456789012345678901234567890123456789.1</br>2345678901234567890123456789012345678901</br>234567890123456789.123456789012345678901</br>2345678901234567890123456789012345678901</br>23.test.org</br> | Valid | ✅</br>in 77030.65 ns | ✅</br>in 24362.25 ns | ✅</br>in 22208.93 ns |
| first.last@3com.com</br> | Valid | ✅</br>in 8708.17 ns | ✅</br>in 5946.46 ns | ✅</br>in 4079.77 ns |
| first.last@123.test.org</br> | Valid | ✅</br>in 9058.53 ns | ✅</br>in 5684.85 ns | ✅</br>in 2853.11 ns |
| first.last@x2345678901234567890123456789</br>0123456789012345678901234567890123.test.</br>org</br> | Valid | ✅</br>in 26641.21 ns | ✅</br>in 12530.28 ns | ✅</br>in 8833.95 ns |
| 1234567890123456789012345678901234567890</br>123456789012345678901234@test.org</br> | Valid | ✅</br>in 23742.88 ns | ✅</br>in 17876.87 ns | ✅</br>in 10839.13 ns |
| email@[123.123.123.123]</br> | Valid | ✅</br>in 7930.52 ns | ✅</br>in 6129.39 ns | ✅</br>in 3619.97 ns |
| first.last@[12.34.56.78]</br> | Valid | ✅</br>in 7652.53 ns | ✅</br>in 7259.05 ns | ✅</br>in 4131.48 ns |
| user+mailbox@test.org</br> | Valid | ✅</br>in 16366.5 ns | ✅</br>in 6334.76 ns | ✅</br>in 3948.77 ns |
| customer/department=shipping@test.org</br> | Valid | ✅</br>in 14863.36 ns | ✅</br>in 12052.43 ns | ✅</br>in 10145.95 ns |
| $A12345@test.org</br> | Valid | ✅</br>in 10385.7 ns | ✅</br>in 5997.2 ns | ✅</br>in 5246.89 ns |
| !def!xyz%abc@test.org</br> | Valid | ✅</br>in 24957.69 ns | ✅</br>in 10217.04 ns | ✅</br>in 10970.85 ns |
| _somename@test.org</br> | Valid | ✅</br>in 13230.58 ns | ✅</br>in 11783.73 ns | ✅</br>in 3613.59 ns |
| dclo@us.ibm.com</br> | Valid | ✅</br>in 12481.04 ns | ✅</br>in 7306.74 ns | ✅</br>in 3310.32 ns |
| test@xn--example.com</br> | Valid | ✅</br>in 16204.19 ns | ✅</br>in 9919.77 ns | ✅</br>in 8063.36 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777:8888]</br> | Valid | ✅</br>in 22160.51 ns | ❌</br>in 15342.87 ns | ✅</br>in 8415.89 ns |
| first.last@[IPv6:1111:2222:3333::4444:55</br>55:6666:7777]</br> | Valid | ✅</br>in 15567.73 ns | ❌</br>in 14096.73 ns | ✅</br>in 10484.27 ns |
| first.last@[IPv6:::1111:2222:3333:4444:5</br>555:6666]</br> | Valid | ✅</br>in 21079.53 ns | ❌</br>in 9794.62 ns | ✅</br>in 5924.67 ns |
| first.last@[IPv6:1111:2222:3333::4444:55</br>55:6666]</br> | Valid | ✅</br>in 9735.3 ns | ❌</br>in 76049.88 ns | ✅</br>in 8746.66 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666::]</br> | Valid | ✅</br>in 8392.78 ns | ❌</br>in 51892.98 ns | ✅</br>in 3656.51 ns |
| first.last@[IPv6:::a2:a3:a4:b1:b2:b3:b4]</br> | Valid | ✅</br>in 7719.97 ns | ❌</br>in 7914.94 ns | ✅</br>in 4412.42 ns |
| first.last@[IPv6:a1:a2:a3:a4:b1:b2:b3::]</br> | Valid | ✅</br>in 9295.89 ns | ❌</br>in 19912.86 ns | ✅</br>in 4918.29 ns |
| first.last@[IPv6:::]</br> | Valid | ✅</br>in 17417.64 ns | ❌</br>in 6190.48 ns | ✅</br>in 3514.03 ns |
| first.last@[IPv6:::b4]</br> | Valid | ✅</br>in 11349.66 ns | ❌</br>in 8001.39 ns | ✅</br>in 4080.96 ns |
| first.last@[IPv6:::b3:b4]</br> | Valid | ✅</br>in 13259.64 ns | ❌</br>in 8508.04 ns | ✅</br>in 4395.68 ns |
| first.last@[IPv6:a1::b4]</br> | Valid | ✅</br>in 10865.43 ns | ❌</br>in 73040.29 ns | ✅</br>in 3902.03 ns |
| first.last@[IPv6:a1::]</br> | Valid | ✅</br>in 7000.33 ns | ❌</br>in 81742.75 ns | ✅</br>in 4039.96 ns |
| first.last@[IPv6:a1:a2::]</br> | Valid | ✅</br>in 7098.7 ns | ❌</br>in 48928.43 ns | ✅</br>in 4002.6 ns |
| first.last@[IPv6:0123:4567:89ab:cdef::]</br> | Valid | ✅</br>in 22989.21 ns | ❌</br>in 48519.51 ns | ✅</br>in 4641.09 ns |
| first.last@[IPv6:0123:4567:89ab:CDEF::]</br> | Valid | ✅</br>in 15166.16 ns | ❌</br>in 56107.76 ns | ✅</br>in 4427.25 ns |
| first.last@[IPv6:::12.34.56.78]</br> | Valid | ✅</br>in 16955.96 ns | ❌</br>in 8095.9 ns | ✅</br>in 6790.95 ns |
| first.last@[IPv6:1111:2222:3333::4444:12</br>.34.56.78]</br> | Valid | ✅</br>in 15592.63 ns | ❌</br>in 50343.32 ns | ✅</br>in 5835.64 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:12.34.56.78]</br> | Valid | ✅</br>in 15911.45 ns | ❌</br>in 50651.25 ns | ✅</br>in 7339.27 ns |
| first.last@[IPv6:1111:2222:3333::4444:55</br>55:12.34.56.78]</br> | Valid | ✅</br>in 14068.09 ns | ❌</br>in 54941.46 ns | ✅</br>in 8455.84 ns |
| aaa@[123.123.123.123]</br> | Valid | ✅</br>in 7016.67 ns | ✅</br>in 6194.23 ns | ✅</br>in 1945.42 ns |
| first.last@[IPv6:::a3:a4:b1:ffff:11.22.3</br>3.44]</br> | Valid | ✅</br>in 9916.2 ns | ❌</br>in 7883.7 ns | ✅</br>in 5449.09 ns |
| first.last@[IPv6:::a2:a3:a4:b1:ffff:11.2</br>2.33.44]</br> | Valid | ✅</br>in 11226.75 ns | ❌</br>in 8600.63 ns | ✅</br>in 5048.78 ns |
| first.last@[IPv6:a1:a2:a3:a4::11.22.33.4</br>4]</br> | Valid | ✅</br>in 16148.53 ns | ❌</br>in 54334.71 ns | ✅</br>in 6562.54 ns |
| first.last@[IPv6:a1:a2:a3:a4:b1::11.22.3</br>3.44]</br> | Valid | ✅</br>in 12416.41 ns | ❌</br>in 43998.55 ns | ✅</br>in 5231.44 ns |
| first.last@[IPv6:a1::11.22.33.44]</br> | Valid | ✅</br>in 9421.01 ns | ❌</br>in 46861.28 ns | ✅</br>in 1684.09 ns |
| first.last@[IPv6:a1:a2::11.22.33.44]</br> | Valid | ✅</br>in 14441.95 ns | ❌</br>in 51577.17 ns | ✅</br>in 1825.21 ns |
| first.last@[IPv6:0123:4567:89ab:cdef::11</br>.22.33.44]</br> | Valid | ✅</br>in 16033.25 ns | ❌</br>in 40854.69 ns | ✅</br>in 1878.79 ns |
| first.last@[IPv6:0123:4567:89ab:CDEF::11</br>.22.33.44]</br> | Valid | ✅</br>in 8956.95 ns | ❌</br>in 43437.33 ns | ✅</br>in 1737.29 ns |
| first.last@[IPv6:a1::b2:11.22.33.44]</br> | Valid | ✅</br>in 7049.71 ns | ❌</br>in 95537.67 ns | ✅</br>in 1625.94 ns |
| +@b.c</br> | Valid | ✅</br>in 5304.48 ns | ❌</br>in 31448.6 ns | ✅</br>in 553.14 ns |
| TEST@test.org</br> | Valid | ✅</br>in 11661.51 ns | ✅</br>in 6170.86 ns | ✅</br>in 1064.06 ns |
| 1234567890@test.org</br> | Valid | ✅</br>in 12559.95 ns | ✅</br>in 7220.43 ns | ✅</br>in 1636.68 ns |
| test-test@test.org</br> | Valid | ✅</br>in 9759.06 ns | ✅</br>in 6212.3 ns | ✅</br>in 1266.2 ns |
| t*est@test.org</br> | Valid | ✅</br>in 9007.84 ns | ✅</br>in 5070.67 ns | ✅</br>in 1092.03 ns |
| +1~1+@test.org</br> | Valid | ✅</br>in 10115.19 ns | ✅</br>in 5528.37 ns | ✅</br>in 926.99 ns |
| {_test_}@test.org</br> | Valid | ✅</br>in 8806.2 ns | ✅</br>in 5615.15 ns | ✅</br>in 1413.69 ns |
| test@123.123.123.x123</br> | Valid | ✅</br>in 18830.72 ns | ❌</br>in 7182.59 ns | ✅</br>in 1782.72 ns |
| cdburgess+!#$%&'*-/=?+_{}&#124;~test@gma</br>il.com</br> | Valid | ✅</br>in 17416.54 ns | ✅</br>in 9747.74 ns | ✅</br>in 2100.82 ns |
| valid@about.museum</br> | Valid | ✅</br>in 27127.68 ns | ✅</br>in 5801.38 ns | ✅</br>in 1339.84 ns |
| a@bar</br> | Valid | ✅</br>in 5721.84 ns | ✅</br>in 5171.13 ns | ✅</br>in 547.49 ns |
| a-b@bar.com</br> | Valid | ✅</br>in 11593.36 ns | ✅</br>in 5169.78 ns | ✅</br>in 1038.98 ns |
| +@b.com</br> | Valid | ✅</br>in 9577.53 ns | ✅</br>in 3968.3 ns | ✅</br>in 1032.34 ns |
| cal(foo\@bar)@iamcal.com</br> | Valid | ✅</br>in 9364.9 ns | ❌</br>in 2791.39 ns | ❌</br>in 37709.48 ns |
| (comment)test@test.org</br> | Valid | ✅</br>in 9237.43 ns | ❌</br>in 1694.89 ns | ✅</br>in 1103.5 ns |
| (foo)cal(bar)@(baz)iamcal.com(quux)</br> | Valid | ✅</br>in 11605.69 ns | ❌</br>in 2952.64 ns | ❌</br>in 31775.83 ns |
| cal(foo\)bar)@iamcal.com</br> | Valid | ✅</br>in 10827.49 ns | ❌</br>in 3005.14 ns | ❌</br>in 37948.81 ns |
| cal(woo(yay)hoopla)@iamcal.com</br> | Valid | ✅</br>in 14068.29 ns | ❌</br>in 3798.48 ns | ❌</br>in 29561.95 ns |
| first(Welcome to&#13;&#10; the ("wonderf</br>ul" (!)) world&#13;&#10; of email)@test.</br>org</br> | Valid | ✅</br>in 28630.28 ns | ❌</br>in 6836.52 ns | ❌</br>in 32994.84 ns |
| pete(his account)@silly.test(his host)</br> | Valid | ✅</br>in 10949.23 ns | ❌</br>in 2751.57 ns | ❌</br>in 75500.2 ns |
| first(abc\(def)@test.org</br> | Valid | ✅</br>in 8576.95 ns | ❌</br>in 3133.53 ns | ❌</br>in 28591.54 ns |
| a(a(b(c)d(e(f))g)h(i)j)@test.org</br> | Valid | ✅</br>in 10004.67 ns | ❌</br>in 2199.84 ns | ❌</br>in 48363.54 ns |
| c@(Chris's host.)public.example</br> | Valid | ✅</br>in 8555.77 ns | ❌</br>in 2070.03 ns | ❌</br>in 52687.91 ns |
| a@b.co-foo.uk</br> | Valid | ✅</br>in 7965.67 ns | ✅</br>in 8437.68 ns | ✅</br>in 1289.89 ns |
| _Yosemite.Sam@test.org</br> | Valid | ✅</br>in 9327.75 ns | ✅</br>in 7316.3 ns | ✅</br>in 1144.92 ns |
| ~@test.org</br> | Valid | ✅</br>in 4854.51 ns | ✅</br>in 3517.93 ns | ✅</br>in 849.06 ns |
| Iinsist@(that comments are allowed)this.</br>is.ok</br> | Valid | ✅</br>in 8932.13 ns | ❌</br>in 3844.81 ns | ❌</br>in 33808.99 ns |
| test@Bücher.ch</br> | Valid | ✅</br>in 47368.18 ns | ✅</br>in 41366.64 ns | ✅</br>in 447.55 ns |
| あいうえお@example.com</br> | Valid | ✅</br>in 6329.19 ns | ✅</br>in 12483.32 ns | ✅</br>in 478.62 ns |
| Pelé@example.com</br> | Valid | ✅</br>in 7578.32 ns | ✅</br>in 7335.92 ns | ✅</br>in 358.96 ns |
| δοκιμή@παράδειγμα.δοκιμή</br> | Valid | ✅</br>in 67608.43 ns | ❌</br>in 157530.66 ns | ✅</br>in 25552.9 ns |
| 我買@屋企.香港</br> | Valid | ✅</br>in 19435.84 ns | ✅</br>in 17107.26 ns | ✅</br>in 4842.93 ns |
| 二ノ宮@黒川.日本</br> | Valid | ✅</br>in 14298.79 ns | ❌</br>in 55145.28 ns | ✅</br>in 4940.76 ns |
| медведь@с-балалайкой.рф</br> | Valid | ✅</br>in 31957.91 ns | ✅</br>in 33030.42 ns | ✅</br>in 11850.75 ns |
| संपर्क@डाटामेल.भारत</br> | Valid | ✅</br>in 26718.09 ns | ✅</br>in 27069.87 ns | ❌</br>in 31382.44 ns |
| email@example.com (Joe Smith)</br> | Valid | ✅</br>in 7650.36 ns | ❌</br>in 1612.75 ns | ✅</br>in 12945.86 ns |
| cal@iamcal(woo).(yay)com</br> | Valid | ✅</br>in 8366.06 ns | ❌</br>in 11126.93 ns | ✅</br>in 7170.3 ns |
| first(abc.def).last@test.org</br> | Valid | ✅</br>in 11981.78 ns | ❌</br>in 7164.6 ns | ❌</br>in 28448.49 ns |
| first(a"bc.def).last@test.org</br> | Valid | ✅</br>in 11148.25 ns | ❌</br>in 7181.99 ns | ❌</br>in 18616.27 ns |
| first.(")middle.last(")@test.org</br> | Valid | ✅</br>in 9383.05 ns | ❌</br>in 4574.2 ns | ❌</br>in 25927.56 ns |
| first.last@x(123456789012345678901234567</br>8901234567890123456789012345678901234567</br>890).com</br> | Valid | ✅</br>in 8250.6 ns | ❌</br>in 35603.01 ns | ✅</br>in 6097.33 ns |
| user%uucp!path@berkeley.edu</br> | Valid | ✅</br>in 9168.33 ns | ✅</br>in 9091.37 ns | ✅</br>in 7244.04 ns |
| first().last@test.org</br> | Valid | ✅</br>in 8247.92 ns | ❌</br>in 4531.4 ns | ❌</br>in 26254.17 ns |
| Totals | 311/311 | 311/311</br>Average Time: 9689.511993569131 ns | 237/311</br>Average Time: 13988.394855305465 ns | 211/311</br>Average Time: 14944.865176848876 ns |
