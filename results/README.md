| Email Address | Expected | JMail | Apache Commons | Javax Mail | email-rfc2822 |
| --- | :---: | :---: | :---: | :---: | :---: |
| "qu@test.org</br> | Invalid | ✅</br>in 7402.84 ns | ✅</br>in 25008.59 ns | ✅</br>in 17426.85 ns | ✅</br>in 148103.92 ns |
| ote"@test.org</br> | Invalid | ✅</br>in 8198.89 ns | ✅</br>in 19203.72 ns | ✅</br>in 18319.63 ns | ✅</br>in 20345.37 ns |
| "(),:;<>[\]@example.com</br> | Invalid | ✅</br>in 9434.03 ns | ✅</br>in 122245.28 ns | ✅</br>in 48198.36 ns | ✅</br>in 39215.81 ns |
| """@iana.org</br> | Invalid | ✅</br>in 2386.22 ns | ✅</br>in 44837.73 ns | ✅</br>in 22551.66 ns | ✅</br>in 17526.79 ns |
| Abc.example.com</br> | Invalid | ✅</br>in 3351.91 ns | ✅</br>in 1614.77 ns | ✅</br>in 34543.68 ns | ✅</br>in 28586.22 ns |
| A@b@c@example.com</br> | Invalid | ✅</br>in 1727.13 ns | ✅</br>in 3669.2 ns | ✅</br>in 48011.62 ns | ✅</br>in 16099.94 ns |
| a"b(c)d,e:f;g<h>i[j\k]l@example.com</br> | Invalid | ✅</br>in 1014.17 ns | ✅</br>in 3589.29 ns | ✅</br>in 24673.97 ns | ✅</br>in 10046.64 ns |
| just"not"right@example.com</br> | Invalid | ✅</br>in 1236.92 ns | ✅</br>in 3294.37 ns | ✅</br>in 25484.02 ns | ✅</br>in 10647.13 ns |
| this is"not\allowed@example.com</br> | Invalid | ✅</br>in 1359.54 ns | ✅</br>in 17081.32 ns | ✅</br>in 20423.11 ns | ✅</br>in 15725.19 ns |
| this\ still\"not\\allowed@example.com</br> | Invalid | ✅</br>in 1513.14 ns | ❌</br>in 28564.22 ns | ✅</br>in 39121.76 ns | ✅</br>in 11895.82 ns |
| 1234567890123456789012345678901234567890</br>123456789012345678901234+x@example.com</br> | Invalid | ✅</br>in 12804.39 ns | ✅</br>in 3356.23 ns | ❌</br>in 20187.11 ns | ❌</br>in 83124.54 ns |
| i_like_underscore@but_its_not_allowed_in</br>_this_part.example.com</br> | Invalid | ✅</br>in 42408.86 ns | ✅</br>in 63428.35 ns | ✅</br>in 25582.75 ns | ❌</br>in 31396.87 ns |
| QA[icon]CHOCOLATE[icon]@test.com</br> | Invalid | ✅</br>in 1060.12 ns | ✅</br>in 3417.14 ns | ✅</br>in 31990.29 ns | ✅</br>in 165646.58 ns |
| plainaddress</br> | Invalid | ✅</br>in 2187.24 ns | ✅</br>in 638.0 ns | ✅</br>in 18635.05 ns | ✅</br>in 18415.85 ns |
| @example.com</br> | Invalid | ✅</br>in 1599.02 ns | ✅</br>in 799.36 ns | ✅</br>in 19005.84 ns | ✅</br>in 7729.04 ns |
| @NotAnEmail</br> | Invalid | ✅</br>in 2091.78 ns | ✅</br>in 996.42 ns | ✅</br>in 21097.69 ns | ✅</br>in 3581.87 ns |
| Joe Smith <email@example.com></br> | Invalid | ✅</br>in 840.61 ns | ✅</br>in 2220.56 ns | ❌</br>in 5035.94 ns | ❌</br>in 9984.89 ns |
| email.example.com</br> | Invalid | ✅</br>in 2552.05 ns | ✅</br>in 1120.39 ns | ✅</br>in 22868.55 ns | ✅</br>in 26559.91 ns |
| email@example@example.com</br> | Invalid | ✅</br>in 2632.65 ns | ✅</br>in 10788.56 ns | ✅</br>in 24265.85 ns | ✅</br>in 49277.13 ns |
| .email@example.com</br> | Invalid | ✅</br>in 127.85 ns | ✅</br>in 2002.14 ns | ✅</br>in 16660.85 ns | ✅</br>in 8092.63 ns |
| email.@example.com</br> | Invalid | ✅</br>in 2919.45 ns | ✅</br>in 3067.71 ns | ✅</br>in 19273.17 ns | ✅</br>in 10634.24 ns |
| email..email@example.com</br> | Invalid | ✅</br>in 1123.51 ns | ✅</br>in 5917.39 ns | ✅</br>in 41552.19 ns | ✅</br>in 23702.4 ns |
| email@-example.com</br> | Invalid | ✅</br>in 2060.7 ns | ✅</br>in 29549.65 ns | ❌</br>in 2703.01 ns | ❌</br>in 22681.93 ns |
| email@111.222.333.44444</br> | Invalid | ✅</br>in 15672.04 ns | ✅</br>in 10463.21 ns | ❌</br>in 9317.23 ns | ❌</br>in 10564.76 ns |
| email@example..com</br> | Invalid | ✅</br>in 1614.3 ns | ✅</br>in 9061.69 ns | ✅</br>in 55844.07 ns | ✅</br>in 14252.52 ns |
| Abc..123@example.com</br> | Invalid | ✅</br>in 992.71 ns | ✅</br>in 2730.04 ns | ✅</br>in 25691.99 ns | ✅</br>in 22853.0 ns |
| just"not"right@example.com</br> | Invalid | ✅</br>in 1024.64 ns | ✅</br>in 2982.79 ns | ✅</br>in 30548.99 ns | ✅</br>in 7866.95 ns |
| this\ is"really"not\allowed@example.com</br> | Invalid | ✅</br>in 974.12 ns | ✅</br>in 4228.59 ns | ✅</br>in 20358.68 ns | ✅</br>in 7636.84 ns |
| first.last@sub.do,com</br> | Invalid | ✅</br>in 18581.5 ns | ✅</br>in 7156.59 ns | ✅</br>in 21763.65 ns | ✅</br>in 22159.36 ns |
| first\@last@iana.org</br> | Invalid | ✅</br>in 955.37 ns | ❌</br>in 4938.33 ns | ❌</br>in 2710.3 ns | ✅</br>in 9567.74 ns |
| email@[12.34.44.56</br> | Invalid | ✅</br>in 1012.21 ns | ✅</br>in 5358.19 ns | ✅</br>in 16138.69 ns | ✅</br>in 811300.99 ns |
| email@14.44.56.34]</br> | Invalid | ✅</br>in 31661.99 ns | ✅</br>in 8204.97 ns | ❌</br>in 8672.41 ns | ✅</br>in 15087.65 ns |
| email@[1.1.23.5f]</br> | Invalid | ✅</br>in 6356.54 ns | ✅</br>in 7988.3 ns | ❌</br>in 1906.24 ns | ❌</br>in 10698.04 ns |
| email@[3.256.255.23]</br> | Invalid | ✅</br>in 2637.27 ns | ✅</br>in 14837.1 ns | ❌</br>in 2404.23 ns | ❌</br>in 13224.61 ns |
| first.last</br> | Invalid | ✅</br>in 1458.16 ns | ✅</br>in 1098.75 ns | ✅</br>in 20042.01 ns | ✅</br>in 17940.73 ns |
| 1234567890123456789012345678901234567890</br>1234567890123456789012345@test.org</br> | Invalid | ✅</br>in 10997.55 ns | ✅</br>in 3479.88 ns | ❌</br>in 15142.35 ns | ❌</br>in 64293.44 ns |
| .first.last@test.org</br> | Invalid | ✅</br>in 104.42 ns | ✅</br>in 2422.95 ns | ✅</br>in 41528.37 ns | ✅</br>in 17174.84 ns |
| first.last.@test.org</br> | Invalid | ✅</br>in 3343.5 ns | ✅</br>in 4125.02 ns | ✅</br>in 21643.26 ns | ✅</br>in 24206.57 ns |
| first..last@test.org</br> | Invalid | ✅</br>in 1502.38 ns | ✅</br>in 2519.98 ns | ✅</br>in 21988.92 ns | ✅</br>in 16494.04 ns |
| "first"last"@test.org</br> | Invalid | ✅</br>in 1209.23 ns | ✅</br>in 2239.13 ns | ✅</br>in 15236.14 ns | ✅</br>in 6875.88 ns |
| first.last@[.12.34.56.78]</br> | Invalid | ✅</br>in 24423.57 ns | ✅</br>in 6264.16 ns | ❌</br>in 2806.05 ns | ❌</br>in 24852.36 ns |
| first.last@[12.34.56.789]</br> | Invalid | ✅</br>in 3595.55 ns | ✅</br>in 9723.4 ns | ❌</br>in 2703.51 ns | ❌</br>in 16544.31 ns |
| first.last@[::12.34.56.78]</br> | Invalid | ✅</br>in 1926.05 ns | ❌</br>in 16692.95 ns | ❌</br>in 9641.64 ns | ❌</br>in 16501.48 ns |
| x@x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456</br> | Invalid | ✅</br>in 25932.06 ns | ✅</br>in 26281.4 ns | ❌</br>in 26612.15 ns | ❌</br>in 51279.59 ns |
| "\"@iana.org</br> | Invalid | ✅</br>in 2037.61 ns | ❌</br>in 3876.54 ns | ✅</br>in 13878.59 ns | ✅</br>in 6963.67 ns |
| first\\@last@iana.org</br> | Invalid | ✅</br>in 709.54 ns | ✅</br>in 1595.74 ns | ❌</br>in 1404.17 ns | ✅</br>in 6222.66 ns |
| first.last@</br> | Invalid | ✅</br>in 1767.16 ns | ✅</br>in 964.72 ns | ✅</br>in 24406.49 ns | ✅</br>in 14358.38 ns |
| test@example.com&#10;</br> | Invalid | ✅</br>in 11594.75 ns | ✅</br>in 5419.38 ns | ✅</br>in 16193.24 ns | ✅</br>in 16989.5 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777]</br> | Invalid | ✅</br>in 11960.65 ns | ✅</br>in 25863.67 ns | ❌</br>in 871.24 ns | ❌</br>in 20243.8 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777:8888:9999]</br> | Invalid | ✅</br>in 9979.85 ns | ✅</br>in 9284.11 ns | ❌</br>in 771.11 ns | ❌</br>in 18456.63 ns |
| first.last@[IPv6:1111:2222::3333::4444:5</br>555:6666]</br> | Invalid | ✅</br>in 3562.42 ns | ✅</br>in 7422.58 ns | ❌</br>in 691.61 ns | ❌</br>in 17171.0 ns |
| first.last@[IPv6:1111:2222:333x::4444:55</br>55]</br> | Invalid | ✅</br>in 2554.47 ns | ✅</br>in 43795.18 ns | ❌</br>in 574.85 ns | ❌</br>in 11914.14 ns |
| first.last@[IPv6:1111:2222:33333::4444:5</br>555]</br> | Invalid | ✅</br>in 2251.45 ns | ✅</br>in 34515.03 ns | ❌</br>in 674.78 ns | ❌</br>in 11645.15 ns |
| first.last@[IPv6::]</br> | Invalid | ✅</br>in 2088.96 ns | ✅</br>in 22522.71 ns | ❌</br>in 354.04 ns | ❌</br>in 10645.69 ns |
| first.last@[IPv6::::]</br> | Invalid | ✅</br>in 1796.53 ns | ✅</br>in 2884.93 ns | ❌</br>in 359.74 ns | ❌</br>in 13865.46 ns |
| first.last@[IPv6::b4]</br> | Invalid | ✅</br>in 2092.36 ns | ✅</br>in 38753.82 ns | ❌</br>in 693.46 ns | ❌</br>in 14034.05 ns |
| first.last@[IPv6::::b4]</br> | Invalid | ✅</br>in 3635.71 ns | ✅</br>in 3912.65 ns | ❌</br>in 458.84 ns | ❌</br>in 14092.14 ns |
| first.last@[IPv6::b3:b4]</br> | Invalid | ✅</br>in 2184.72 ns | ✅</br>in 21819.27 ns | ❌</br>in 381.44 ns | ❌</br>in 15945.18 ns |
| first.last@[IPv6::::b3:b4]</br> | Invalid | ✅</br>in 2032.14 ns | ✅</br>in 5673.58 ns | ❌</br>in 541.66 ns | ❌</br>in 13506.83 ns |
| first.last@[IPv6:a1:::b4]</br> | Invalid | ✅</br>in 2604.06 ns | ✅</br>in 5302.66 ns | ❌</br>in 713.01 ns | ❌</br>in 15723.47 ns |
| first.last@[IPv6:a1:]</br> | Invalid | ✅</br>in 1222.68 ns | ✅</br>in 3732.5 ns | ❌</br>in 446.82 ns | ❌</br>in 14272.73 ns |
| first.last@[IPv6:a1:::]</br> | Invalid | ✅</br>in 2524.92 ns | ✅</br>in 4306.68 ns | ❌</br>in 478.74 ns | ❌</br>in 19547.84 ns |
| first.last@[IPv6:a1:a2:]</br> | Invalid | ✅</br>in 2016.77 ns | ✅</br>in 3965.37 ns | ❌</br>in 493.61 ns | ❌</br>in 16534.99 ns |
| first.last@[IPv6:a1:a2:::]</br> | Invalid | ✅</br>in 2419.21 ns | ✅</br>in 6085.81 ns | ❌</br>in 556.38 ns | ❌</br>in 21117.19 ns |
| first.last@[IPv6:a1::b3:]</br> | Invalid | ✅</br>in 2177.39 ns | ✅</br>in 4171.68 ns | ❌</br>in 534.34 ns | ❌</br>in 16704.13 ns |
| first.last@[IPv6::a2::b4]</br> | Invalid | ✅</br>in 5447.43 ns | ✅</br>in 8929.22 ns | ❌</br>in 607.74 ns | ❌</br>in 21290.2 ns |
| first.last@[IPv6:a1:a2:a3:a4:b1:b2:b3:]</br> | Invalid | ✅</br>in 4832.32 ns | ✅</br>in 5347.9 ns | ❌</br>in 730.29 ns | ❌</br>in 19010.65 ns |
| first.last@[IPv6::a2:a3:a4:b1:b2:b3:b4]</br> | Invalid | ✅</br>in 6366.83 ns | ✅</br>in 11394.13 ns | ❌</br>in 583.68 ns | ❌</br>in 19753.17 ns |
| first.last@[IPv6:a1:a2:a3:a4::b1:b2:b3:b</br>4]</br> | Invalid | ✅</br>in 6600.99 ns | ✅</br>in 9809.72 ns | ❌</br>in 875.3 ns | ❌</br>in 18599.9 ns |
| first.last@[IPv6::11.22.33.44]</br> | Invalid | ✅</br>in 5513.11 ns | ✅</br>in 32198.97 ns | ❌</br>in 619.47 ns | ❌</br>in 16353.65 ns |
| first.last@[IPv6::::11.22.33.44]</br> | Invalid | ✅</br>in 3165.36 ns | ✅</br>in 5976.93 ns | ❌</br>in 694.18 ns | ❌</br>in 18577.87 ns |
| first.last@[IPv6:a1:11.22.33.44]</br> | Invalid | ✅</br>in 27736.18 ns | ✅</br>in 26403.09 ns | ❌</br>in 681.45 ns | ❌</br>in 19391.29 ns |
| first.last@[IPv6:a1:::11.22.33.44]</br> | Invalid | ✅</br>in 2095.62 ns | ✅</br>in 5575.08 ns | ❌</br>in 644.12 ns | ❌</br>in 19914.14 ns |
| first.last@[IPv6:a1:a2:::11.22.33.44]</br> | Invalid | ✅</br>in 3980.6 ns | ✅</br>in 3551.35 ns | ❌</br>in 584.12 ns | ❌</br>in 15850.09 ns |
| first.last@[IPv6:0123:4567:89ab:cdef::11</br>.22.33.xx]</br> | Invalid | ✅</br>in 5109.52 ns | ✅</br>in 33323.48 ns | ❌</br>in 793.85 ns | ❌</br>in 19646.82 ns |
| first.last@[IPv5:::12.34.56.78]</br> | Invalid | ✅</br>in 9247.84 ns | ✅</br>in 9182.24 ns | ❌</br>in 4778.57 ns | ❌</br>in 18589.09 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:12.34.56.78]</br> | Invalid | ✅</br>in 5078.97 ns | ✅</br>in 35712.72 ns | ❌</br>in 890.25 ns | ❌</br>in 19777.61 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777:12.34.56.78]</br> | Invalid | ✅</br>in 4701.78 ns | ✅</br>in 10192.01 ns | ❌</br>in 915.76 ns | ❌</br>in 18757.09 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:12.34.567.89]</br> | Invalid | ✅</br>in 6168.91 ns | ✅</br>in 30215.7 ns | ❌</br>in 907.91 ns | ❌</br>in 18891.58 ns |
| aaa@[123.123.123.123]a</br> | Invalid | ✅</br>in 658.54 ns | ✅</br>in 4358.96 ns | ✅</br>in 75059.26 ns | ✅</br>in 1.199018155E7 ns |
| aaa@[123.123.123.333]</br> | Invalid | ✅</br>in 1644.59 ns | ✅</br>in 3777.88 ns | ❌</br>in 905.32 ns | ❌</br>in 6913.98 ns |
| first.last@[IPv6:0123:4567:89ab:CDEFF::1</br>1.22.33.44]</br> | Invalid | ✅</br>in 1802.83 ns | ✅</br>in 34811.72 ns | ❌</br>in 3326.56 ns | ❌</br>in 16686.32 ns |
| first.last@[IPv6:a1::a4:b1::b4:11.22.33.</br>44]</br> | Invalid | ✅</br>in 2348.56 ns | ✅</br>in 6257.08 ns | ❌</br>in 3217.38 ns | ❌</br>in 16767.26 ns |
| first.last@[IPv6:a1::11.22.33]</br> | Invalid | ✅</br>in 3646.15 ns | ✅</br>in 32269.35 ns | ❌</br>in 2365.01 ns | ❌</br>in 17302.57 ns |
| first.last@[IPv6:a1::11.22.33.44.55]</br> | Invalid | ✅</br>in 6139.73 ns | ✅</br>in 44353.61 ns | ❌</br>in 2811.91 ns | ❌</br>in 14928.8 ns |
| first.last@[IPv6:a1::b211.22.33.44]</br> | Invalid | ✅</br>in 3032.78 ns | ✅</br>in 22166.39 ns | ❌</br>in 1625.36 ns | ❌</br>in 10979.75 ns |
| first.last@[IPv6:a1::b2::11.22.33.44]</br> | Invalid | ✅</br>in 2477.65 ns | ✅</br>in 5637.36 ns | ❌</br>in 2418.45 ns | ❌</br>in 15783.04 ns |
| abc\@def@test.org</br> | Invalid | ✅</br>in 1168.97 ns | ❌</br>in 4561.91 ns | ❌</br>in 1811.13 ns | ✅</br>in 9275.12 ns |
| a@-b.com</br> | Invalid | ✅</br>in 1711.43 ns | ✅</br>in 3172.42 ns | ❌</br>in 1054.59 ns | ❌</br>in 7315.56 ns |
| a@b-.com</br> | Invalid | ✅</br>in 678.51 ns | ✅</br>in 2657.95 ns | ❌</br>in 1106.77 ns | ❌</br>in 6274.38 ns |
| -@..com</br> | Invalid | ✅</br>in 573.74 ns | ✅</br>in 4567.01 ns | ✅</br>in 18033.59 ns | ✅</br>in 3768.6 ns |
| -@a..com</br> | Invalid | ✅</br>in 531.41 ns | ✅</br>in 3263.55 ns | ✅</br>in 16451.31 ns | ✅</br>in 6469.32 ns |
| invalid@about.museum-</br> | Invalid | ✅</br>in 119.45 ns | ✅</br>in 6786.12 ns | ❌</br>in 2512.68 ns | ❌</br>in 10336.88 ns |
| test@...........com</br> | Invalid | ✅</br>in 1014.89 ns | ✅</br>in 2753.1 ns | ✅</br>in 25402.65 ns | ✅</br>in 8616.36 ns |
| first.last@-xample.com</br> | Invalid | ✅</br>in 2281.33 ns | ✅</br>in 2729.2 ns | ❌</br>in 1662.84 ns | ❌</br>in 9933.31 ns |
| first.last@exampl-.com</br> | Invalid | ✅</br>in 2768.96 ns | ✅</br>in 20245.08 ns | ❌</br>in 2665.09 ns | ❌</br>in 13131.78 ns |
| first.last@x2345678901234567890123456789</br>01234567890123456789012345678901234.test</br>.org</br> | Invalid | ✅</br>in 11099.8 ns | ✅</br>in 10384.89 ns | ❌</br>in 9872.38 ns | ❌</br>in 20464.6 ns |
| abc\\@test.org</br> | Invalid | ✅</br>in 866.45 ns | ❌</br>in 5329.51 ns | ✅</br>in 16116.62 ns | ✅</br>in 134355.07 ns |
| abc@def@test.org</br> | Invalid | ✅</br>in 763.44 ns | ✅</br>in 1972.01 ns | ✅</br>in 20426.52 ns | ✅</br>in 9552.06 ns |
| abc\\@def@test.org</br> | Invalid | ✅</br>in 475.29 ns | ✅</br>in 1279.95 ns | ❌</br>in 1315.63 ns | ✅</br>in 6022.43 ns |
| abc\@test.org</br> | Invalid | ✅</br>in 736.25 ns | ✅</br>in 1339.78 ns | ✅</br>in 16180.97 ns | ✅</br>in 5483.6 ns |
| @test.org</br> | Invalid | ✅</br>in 1351.19 ns | ✅</br>in 627.79 ns | ✅</br>in 14247.52 ns | ✅</br>in 3241.12 ns |
| doug@</br> | Invalid | ✅</br>in 758.36 ns | ✅</br>in 367.31 ns | ✅</br>in 14215.2 ns | ✅</br>in 9771.9 ns |
| .dot@test.org</br> | Invalid | ✅</br>in 89.61 ns | ✅</br>in 1225.77 ns | ✅</br>in 16057.94 ns | ✅</br>in 6823.76 ns |
| dot.@test.org</br> | Invalid | ✅</br>in 1240.93 ns | ✅</br>in 1184.48 ns | ✅</br>in 15895.37 ns | ✅</br>in 7038.78 ns |
| two..dot@test.org</br> | Invalid | ✅</br>in 513.75 ns | ✅</br>in 1189.66 ns | ✅</br>in 16926.38 ns | ✅</br>in 10143.28 ns |
| "Doug "Ace" L."@test.org</br> | Invalid | ✅</br>in 818.84 ns | ✅</br>in 1262.27 ns | ✅</br>in 13209.56 ns | ✅</br>in 5382.43 ns |
| Doug\ \"Ace\"\ L\.@test.org</br> | Invalid | ✅</br>in 530.47 ns | ❌</br>in 3404.84 ns | ✅</br>in 17004.62 ns | ✅</br>in 7117.99 ns |
| hello world@test.org</br> | Invalid | ✅</br>in 1075.29 ns | ✅</br>in 3004.54 ns | ✅</br>in 11660.5 ns | ✅</br>in 10893.96 ns |
| gatsby@f.sc.ot.t.f.i.tzg.era.l.d.</br> | Invalid | ✅</br>in 108.5 ns | ✅</br>in 58.13 ns | ✅</br>in 12343.02 ns | ✅</br>in 13022.48 ns |
| .@</br> | Invalid | ✅</br>in 59.93 ns | ✅</br>in 267.02 ns | ✅</br>in 24038.95 ns | ✅</br>in 4014.32 ns |
| @bar.com</br> | Invalid | ✅</br>in 1046.75 ns | ✅</br>in 496.37 ns | ✅</br>in 20485.28 ns | ✅</br>in 3162.04 ns |
| @@bar.com</br> | Invalid | ✅</br>in 193.7 ns | ✅</br>in 635.73 ns | ✅</br>in 16987.93 ns | ✅</br>in 2619.58 ns |
| aaa.com</br> | Invalid | ✅</br>in 1079.03 ns | ✅</br>in 456.22 ns | ✅</br>in 16883.08 ns | ✅</br>in 10727.72 ns |
| aaa@.com</br> | Invalid | ✅</br>in 696.64 ns | ✅</br>in 1600.33 ns | ✅</br>in 15009.37 ns | ✅</br>in 6699.46 ns |
| aaa@.123</br> | Invalid | ✅</br>in 713.04 ns | ✅</br>in 1896.31 ns | ✅</br>in 21585.21 ns | ✅</br>in 7738.74 ns |
| a@bar.com.</br> | Invalid | ✅</br>in 70.73 ns | ✅</br>in 81.4 ns | ✅</br>in 15614.28 ns | ✅</br>in 6893.66 ns |
| {^c\@**Dog^}@cartoon.com</br> | Invalid | ✅</br>in 488.75 ns | ❌</br>in 2779.9 ns | ❌</br>in 1572.79 ns | ✅</br>in 5516.28 ns |
| cal(foo(bar)@iamcal.com</br> | Invalid | ✅</br>in 3614.02 ns | ✅</br>in 1825.8 ns | ✅</br>in 21950.8 ns | ✅</br>in 18090.57 ns |
| cal(foo)bar)@iamcal.com</br> | Invalid | ✅</br>in 1924.92 ns | ✅</br>in 1745.26 ns | ✅</br>in 12559.2 ns | ✅</br>in 15578.25 ns |
| cal(foo\)@iamcal.com</br> | Invalid | ✅</br>in 1925.94 ns | ✅</br>in 2193.4 ns | ✅</br>in 16742.33 ns | ✅</br>in 32802.23 ns |
| first(1234567890123456789012345678901234</br>5678901234567890)last@(12345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>8901234567890123456789012345678901234567</br>890123456789012345678901234567890)test.o</br>rg</br> | Invalid | ✅</br>in 64.67 ns | ✅</br>in 19257.02 ns | ✅</br>in 13475.29 ns | ✅</br>in 84587.87 ns |
| a(a(b(c)d(e(f))g)(h(i)j)@test.org</br> | Invalid | ✅</br>in 3068.24 ns | ✅</br>in 2726.97 ns | ✅</br>in 19056.97 ns | ✅</br>in 8554.35 ns |
| Doug\ \"Ace\"\ Lovell@test.org</br> | Invalid | ✅</br>in 872.6 ns | ❌</br>in 5790.05 ns | ✅</br>in 21019.2 ns | ✅</br>in 7557.57 ns |
| test.test.org</br> | Invalid | ✅</br>in 1854.11 ns | ✅</br>in 976.54 ns | ✅</br>in 14286.99 ns | ✅</br>in 14720.76 ns |
| test.@test.org</br> | Invalid | ✅</br>in 1906.71 ns | ✅</br>in 1770.47 ns | ✅</br>in 17789.59 ns | ✅</br>in 8830.88 ns |
| test..test@test.org</br> | Invalid | ✅</br>in 821.78 ns | ✅</br>in 2030.42 ns | ✅</br>in 16871.91 ns | ✅</br>in 15318.02 ns |
| .test@test.org</br> | Invalid | ✅</br>in 88.61 ns | ✅</br>in 1309.71 ns | ✅</br>in 17416.24 ns | ✅</br>in 4398.59 ns |
| test@test@test.org</br> | Invalid | ✅</br>in 1467.91 ns | ✅</br>in 2069.6 ns | ✅</br>in 19564.22 ns | ✅</br>in 15517.35 ns |
| test@@test.org</br> | Invalid | ✅</br>in 709.04 ns | ✅</br>in 1199.93 ns | ✅</br>in 13619.53 ns | ✅</br>in 8616.66 ns |
| -- test --@test.org</br> | Invalid | ✅</br>in 803.1 ns | ✅</br>in 1779.03 ns | ✅</br>in 17380.66 ns | ✅</br>in 15301.6 ns |
| [test]@test.org</br> | Invalid | ✅</br>in 512.16 ns | ✅</br>in 1048.28 ns | ✅</br>in 38773.98 ns | ✅</br>in 8498.74 ns |
| "test"test"@test.org</br> | Invalid | ✅</br>in 980.78 ns | ✅</br>in 1713.97 ns | ✅</br>in 18656.06 ns | ✅</br>in 6790.28 ns |
| ()[]\;:,><@test.org</br> | Invalid | ✅</br>in 2715.21 ns | ✅</br>in 1107.49 ns | ✅</br>in 18701.45 ns | ✅</br>in 13953.68 ns |
| test@.</br> | Invalid | ✅</br>in 108.65 ns | ✅</br>in 66.22 ns | ✅</br>in 27101.77 ns | ✅</br>in 11357.97 ns |
| test@example.</br> | Invalid | ✅</br>in 90.94 ns | ✅</br>in 74.54 ns | ✅</br>in 19011.39 ns | ✅</br>in 13110.66 ns |
| test@.org</br> | Invalid | ✅</br>in 727.08 ns | ✅</br>in 1928.35 ns | ✅</br>in 16375.41 ns | ✅</br>in 6246.57 ns |
| test@12345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>6789012345678901234567890123456789012345</br>67890123456789012.com</br> | Invalid | ✅</br>in 23321.78 ns | ✅</br>in 25162.17 ns | ❌</br>in 28826.31 ns | ❌</br>in 26728.39 ns |
| .wooly@test.org</br> | Invalid | ✅</br>in 72.88 ns | ✅</br>in 1100.59 ns | ✅</br>in 16673.16 ns | ✅</br>in 9715.46 ns |
| wo..oly@test.org</br> | Invalid | ✅</br>in 696.28 ns | ✅</br>in 1826.65 ns | ✅</br>in 19777.55 ns | ✅</br>in 9301.74 ns |
| pootieshoe.@test.org</br> | Invalid | ✅</br>in 1851.36 ns | ✅</br>in 1972.07 ns | ✅</br>in 12948.85 ns | ✅</br>in 14444.82 ns |
| .@test.org</br> | Invalid | ✅</br>in 83.07 ns | ✅</br>in 1155.41 ns | ✅</br>in 27324.66 ns | ✅</br>in 4684.62 ns |
| Ima Fool@test.org</br> | Invalid | ✅</br>in 779.51 ns | ✅</br>in 2012.33 ns | ✅</br>in 14229.89 ns | ✅</br>in 11533.61 ns |
| phil.h\@\@ck@haacked.com</br> | Invalid | ✅</br>in 1260.59 ns | ❌</br>in 4596.65 ns | ❌</br>in 1159.62 ns | ✅</br>in 10199.16 ns |
| "first\\"last"@test.org</br> | Invalid | ✅</br>in 1261.56 ns | ❌</br>in 4842.63 ns | ✅</br>in 17810.11 ns | ✅</br>in 10268.05 ns |
| foo@[\1.2.3.4]</br> | Invalid | ✅</br>in 687.16 ns | ✅</br>in 2966.73 ns | ❌</br>in 708.23 ns | ❌</br>in 5802.11 ns |
| first\last@test.org</br> | Invalid | ✅</br>in 978.91 ns | ❌</br>in 3871.88 ns | ❌</br>in 973.68 ns | ✅</br>in 9573.83 ns |
| Abc\@def@test.org</br> | Invalid | ✅</br>in 780.46 ns | ❌</br>in 3526.32 ns | ❌</br>in 862.01 ns | ✅</br>in 8449.11 ns |
| Fred\ Bloggs@test.org</br> | Invalid | ✅</br>in 855.74 ns | ❌</br>in 3761.21 ns | ❌</br>in 1265.98 ns | ✅</br>in 7802.72 ns |
| Joe.\\Blow@test.org</br> | Invalid | ✅</br>in 2027.72 ns | ❌</br>in 4722.38 ns | ❌</br>in 786.79 ns | ✅</br>in 8301.07 ns |
| first(abc("def".ghi).mno)middle(abc("def</br>".ghi).mno).last@(abc("def".ghi).mno)exa</br>mple(abc("def".ghi).mno).(abc("def".ghi)</br>.mno)com(abc("def".ghi).mno)</br> | Invalid | ✅</br>in 4160.52 ns | ✅</br>in 7915.72 ns | ✅</br>in 32818.63 ns | ✅</br>in 17132.55 ns |
| first(middle)last@test.org</br> | Invalid | ✅</br>in 1390.65 ns | ✅</br>in 2128.31 ns | ✅</br>in 18657.78 ns | ✅</br>in 23034.31 ns |
| "Unicode NULL ␀"@char.com</br> | Invalid | ✅</br>in 2346.1 ns | ❌</br>in 5998.11 ns | ❌</br>in 4282.07 ns | ✅</br>in 68488.7 ns |
| Unicode NULL \␀@char.com</br> | Invalid | ✅</br>in 1385.76 ns | ✅</br>in 4109.89 ns | ✅</br>in 18809.61 ns | ✅</br>in 34178.7 ns |
| "test"test@test.com</br> | Invalid | ✅</br>in 1020.24 ns | ✅</br>in 1910.81 ns | ✅</br>in 20044.06 ns | ✅</br>in 7735.32 ns |
| first."".last@test.org</br> | Invalid | ✅</br>in 1378.34 ns | ❌</br>in 4962.11 ns | ✅</br>in 17349.62 ns | ✅</br>in 12399.45 ns |
| ""@test.org</br> | Invalid | ✅</br>in 673.67 ns | ❌</br>in 3466.43 ns | ❌</br>in 1382.44 ns | ❌</br>in 7663.25 ns |
| ()@test.com</br> | Invalid | ✅</br>in 1082.35 ns | ✅</br>in 823.96 ns | ✅</br>in 18692.33 ns | ✅</br>in 9855.27 ns |
| " "@example.org</br> | Valid | ✅</br>in 7762.31 ns | ✅</br>in 2667.75 ns | ✅</br>in 2341.18 ns | ✅</br>in 12818.47 ns |
| "john..doe"@example.org</br> | Valid | ✅</br>in 10989.85 ns | ✅</br>in 4405.17 ns | ❌</br>in 15944.12 ns | ✅</br>in 9742.53 ns |
| "email"@example.com</br> | Valid | ✅</br>in 4267.22 ns | ✅</br>in 3906.64 ns | ✅</br>in 2158.5 ns | ✅</br>in 9909.03 ns |
| "first@last"@test.org</br> | Valid | ✅</br>in 6551.73 ns | ✅</br>in 4049.88 ns | ✅</br>in 2425.7 ns | ✅</br>in 18602.44 ns |
| very.unusual."@".unusual.com@example.com</br> | Valid | ✅</br>in 6207.33 ns | ✅</br>in 5535.75 ns | ❌</br>in 16496.78 ns | ❌</br>in 17695.5 ns |
| "first\"last"@test.org</br> | Valid | ✅</br>in 7134.09 ns | ✅</br>in 4371.84 ns | ✅</br>in 4167.16 ns | ✅</br>in 17061.46 ns |
| much."more\ unusual"@example.com</br> | Valid | ✅</br>in 5980.76 ns | ✅</br>in 5061.31 ns | ❌</br>in 22341.79 ns | ❌</br>in 10950.2 ns |
| "first\\last"@test.org</br> | Valid | ✅</br>in 4753.31 ns | ✅</br>in 3719.73 ns | ✅</br>in 2275.5 ns | ✅</br>in 19203.54 ns |
| "Abc\@def"@test.org</br> | Valid | ✅</br>in 3634.91 ns | ✅</br>in 2284.77 ns | ✅</br>in 1167.11 ns | ✅</br>in 11378.35 ns |
| "Fred\ Bloggs"@test.org</br> | Valid | ✅</br>in 5252.57 ns | ✅</br>in 4311.62 ns | ✅</br>in 2318.86 ns | ✅</br>in 14972.96 ns |
| "Joe.\\Blow"@test.org</br> | Valid | ✅</br>in 8066.05 ns | ✅</br>in 2958.77 ns | ✅</br>in 1248.82 ns | ✅</br>in 9259.16 ns |
| "Abc@def"@test.org</br> | Valid | ✅</br>in 5205.31 ns | ✅</br>in 2774.1 ns | ✅</br>in 1219.9 ns | ✅</br>in 8660.27 ns |
| "Fred Bloggs"@test.org</br> | Valid | ✅</br>in 6475.73 ns | ✅</br>in 4661.78 ns | ✅</br>in 2350.66 ns | ✅</br>in 15959.38 ns |
| "first\last"@test.org</br> | Valid | ✅</br>in 6648.91 ns | ✅</br>in 4004.81 ns | ✅</br>in 1775.51 ns | ✅</br>in 20395.07 ns |
| "Doug \"Ace\" L."@test.org</br> | Valid | ✅</br>in 6762.73 ns | ✅</br>in 3208.19 ns | ✅</br>in 1418.39 ns | ✅</br>in 15890.07 ns |
| "[[ test ]]"@test.org</br> | Valid | ✅</br>in 6405.5 ns | ✅</br>in 3163.29 ns | ✅</br>in 1331.58 ns | ✅</br>in 14874.01 ns |
| "test.test"@test.org</br> | Valid | ✅</br>in 5399.33 ns | ✅</br>in 3809.78 ns | ✅</br>in 1805.87 ns | ✅</br>in 18425.3 ns |
| test."test"@test.org</br> | Valid | ✅</br>in 10336.98 ns | ✅</br>in 4283.78 ns | ❌</br>in 18342.24 ns | ❌</br>in 11346.02 ns |
| "test@test"@test.org</br> | Valid | ✅</br>in 5939.77 ns | ✅</br>in 4000.72 ns | ✅</br>in 2247.15 ns | ✅</br>in 16360.01 ns |
| "test\test"@test.org</br> | Valid | ✅</br>in 6237.11 ns | ✅</br>in 4168.65 ns | ✅</br>in 2160.84 ns | ✅</br>in 14613.6 ns |
| "first"."last"@test.org</br> | Valid | ✅</br>in 9970.34 ns | ✅</br>in 6100.95 ns | ❌</br>in 18741.95 ns | ❌</br>in 10248.28 ns |
| "first".middle."last"@test.org</br> | Valid | ✅</br>in 7839.27 ns | ✅</br>in 5635.25 ns | ❌</br>in 18100.8 ns | ❌</br>in 11236.0 ns |
| "first".last@test.org</br> | Valid | ✅</br>in 4533.3 ns | ✅</br>in 2555.79 ns | ❌</br>in 11989.47 ns | ❌</br>in 6099.74 ns |
| first."last"@test.org</br> | Valid | ✅</br>in 3622.99 ns | ✅</br>in 2570.32 ns | ❌</br>in 13480.62 ns | ❌</br>in 13309.06 ns |
| "first"."middle"."last"@test.org</br> | Valid | ✅</br>in 7182.16 ns | ✅</br>in 6656.58 ns | ❌</br>in 20197.1 ns | ❌</br>in 6955.2 ns |
| "first.middle"."last"@test.org</br> | Valid | ✅</br>in 7075.51 ns | ✅</br>in 4790.58 ns | ❌</br>in 19521.29 ns | ❌</br>in 13438.11 ns |
| "first.middle.last"@test.org</br> | Valid | ✅</br>in 6956.97 ns | ✅</br>in 4693.31 ns | ✅</br>in 2057.31 ns | ✅</br>in 10971.37 ns |
| "first..last"@test.org</br> | Valid | ✅</br>in 6052.44 ns | ✅</br>in 2846.82 ns | ❌</br>in 81234.8 ns | ✅</br>in 11005.34 ns |
| "Unicode NULL \␀"@char.com</br> | Valid | ✅</br>in 7426.36 ns | ✅</br>in 10711.67 ns | ✅</br>in 1742.3 ns | ❌</br>in 11576.41 ns |
| "test\\blah"@test.org</br> | Valid | ✅</br>in 5983.65 ns | ✅</br>in 3049.04 ns | ✅</br>in 1482.03 ns | ✅</br>in 13585.65 ns |
| "test\blah"@test.org</br> | Valid | ✅</br>in 5690.79 ns | ✅</br>in 3814.47 ns | ✅</br>in 1616.79 ns | ✅</br>in 11504.59 ns |
| "test\"blah"@test.org</br> | Valid | ✅</br>in 5722.8 ns | ✅</br>in 3825.22 ns | ✅</br>in 1505.81 ns | ✅</br>in 10265.4 ns |
| "first\\\"last"@test.org</br> | Valid | ✅</br>in 6220.41 ns | ✅</br>in 3648.57 ns | ✅</br>in 1733.52 ns | ✅</br>in 12461.72 ns |
| first."mid\dle"."last"@test.org</br> | Valid | ✅</br>in 11159.87 ns | ✅</br>in 5933.99 ns | ❌</br>in 18402.85 ns | ❌</br>in 6557.06 ns |
| "Test \"Fail\" Ing"@test.org</br> | Valid | ✅</br>in 4593.65 ns | ✅</br>in 3063.47 ns | ✅</br>in 2182.63 ns | ✅</br>in 13293.04 ns |
| "test&#13;&#10; blah"@test.org</br> | Valid | ✅</br>in 6971.6 ns | ✅</br>in 3652.41 ns | ✅</br>in 2235.63 ns | ✅</br>in 14544.39 ns |
| first.last @test.org</br> | Valid | ✅</br>in 6349.7 ns | ❌</br>in 3559.13 ns | ❌</br>in 21964.03 ns | ✅</br>in 17764.22 ns |
| first.last  @test.org</br> | Valid | ✅</br>in 6440.0 ns | ❌</br>in 3092.28 ns | ❌</br>in 17710.96 ns | ✅</br>in 17818.57 ns |
| first .last  @test .org</br> | Valid | ✅</br>in 8647.76 ns | ❌</br>in 1890.15 ns | ❌</br>in 26182.43 ns | ❌</br>in 23495.2 ns |
| jdoe@machine(comment).  example</br> | Valid | ✅</br>in 7717.43 ns | ❌</br>in 2577.89 ns | ✅</br>in 3057.96 ns | ❌</br>in 17625.45 ns |
| simple@example.com</br> | Valid | ✅</br>in 3833.93 ns | ✅</br>in 2761.12 ns | ✅</br>in 1457.21 ns | ✅</br>in 10948.78 ns |
| very.common@example.com</br> | Valid | ✅</br>in 11350.19 ns | ✅</br>in 4692.47 ns | ✅</br>in 3039.0 ns | ✅</br>in 13648.35 ns |
| very.common@example.org</br> | Valid | ✅</br>in 5959.55 ns | ✅</br>in 4714.66 ns | ✅</br>in 2517.35 ns | ✅</br>in 17308.18 ns |
| disposable.style.email.with+symbol@examp</br>le.com</br> | Valid | ✅</br>in 12166.46 ns | ✅</br>in 10394.41 ns | ✅</br>in 3251.34 ns | ✅</br>in 45837.47 ns |
| other.email-with-hyphen@example.com</br> | Valid | ✅</br>in 9807.19 ns | ✅</br>in 8239.91 ns | ✅</br>in 1408.96 ns | ✅</br>in 31184.51 ns |
| fully-qualified-domain@example.com</br> | Valid | ✅</br>in 4598.88 ns | ✅</br>in 4730.68 ns | ✅</br>in 1364.4 ns | ✅</br>in 20082.63 ns |
| user.name+tag+sorting@example.com</br> | Valid | ✅</br>in 5184.18 ns | ✅</br>in 4834.69 ns | ✅</br>in 1381.21 ns | ✅</br>in 25347.52 ns |
| x@example.com</br> | Valid | ✅</br>in 5317.26 ns | ✅</br>in 3092.61 ns | ✅</br>in 418.03 ns | ✅</br>in 4157.34 ns |
| example-indeed@strange-example.com</br> | Valid | ✅</br>in 32961.89 ns | ✅</br>in 4806.96 ns | ✅</br>in 977.32 ns | ✅</br>in 12674.88 ns |
| test/test@test.com</br> | Valid | ✅</br>in 3730.22 ns | ✅</br>in 3863.03 ns | ✅</br>in 596.39 ns | ✅</br>in 9305.92 ns |
| admin@mailserver1</br> | Valid | ✅</br>in 4756.54 ns | ✅</br>in 3319.79 ns | ✅</br>in 860.52 ns | ✅</br>in 15877.01 ns |
| example@s.example</br> | Valid | ✅</br>in 5248.33 ns | ❌</br>in 4721.34 ns | ✅</br>in 841.04 ns | ✅</br>in 12092.09 ns |
| mailhost!username@example.org</br> | Valid | ✅</br>in 7403.88 ns | ✅</br>in 5582.17 ns | ✅</br>in 1157.41 ns | ✅</br>in 23263.99 ns |
| user%example.com@example.org</br> | Valid | ✅</br>in 6168.56 ns | ✅</br>in 5748.89 ns | ✅</br>in 996.36 ns | ✅</br>in 22978.27 ns |
| user-@example.org</br> | Valid | ✅</br>in 5157.36 ns | ✅</br>in 3361.76 ns | ✅</br>in 872.22 ns | ✅</br>in 11884.73 ns |
| email@example.com</br> | Valid | ✅</br>in 6115.6 ns | ✅</br>in 4017.29 ns | ✅</br>in 719.57 ns | ✅</br>in 11236.11 ns |
| firstname.lastname@example.com</br> | Valid | ✅</br>in 5618.11 ns | ✅</br>in 5662.19 ns | ✅</br>in 1792.07 ns | ✅</br>in 22977.38 ns |
| email@subdomain.example.com</br> | Valid | ✅</br>in 6079.03 ns | ✅</br>in 7729.99 ns | ✅</br>in 1657.71 ns | ✅</br>in 16651.42 ns |
| firstname+lastname@example.com</br> | Valid | ✅</br>in 6651.68 ns | ✅</br>in 5300.16 ns | ✅</br>in 1286.0 ns | ✅</br>in 19867.7 ns |
| 1234567890@example.com</br> | Valid | ✅</br>in 5863.14 ns | ✅</br>in 4181.64 ns | ✅</br>in 978.17 ns | ✅</br>in 14345.5 ns |
| email@example-one.com</br> | Valid | ✅</br>in 5077.73 ns | ✅</br>in 3796.11 ns | ✅</br>in 905.69 ns | ✅</br>in 8752.13 ns |
| _______@example.com</br> | Valid | ✅</br>in 4012.07 ns | ✅</br>in 4360.67 ns | ✅</br>in 821.9 ns | ✅</br>in 18099.13 ns |
| email@example.name</br> | Valid | ✅</br>in 3660.05 ns | ✅</br>in 3269.27 ns | ✅</br>in 854.72 ns | ✅</br>in 9476.68 ns |
| email@example.museum</br> | Valid | ✅</br>in 5524.13 ns | ✅</br>in 4190.87 ns | ✅</br>in 943.5 ns | ✅</br>in 11477.12 ns |
| email@example.co.jp</br> | Valid | ✅</br>in 8195.65 ns | ✅</br>in 5006.58 ns | ✅</br>in 1640.77 ns | ✅</br>in 10111.71 ns |
| firstname-lastname@example.com</br> | Valid | ✅</br>in 6962.85 ns | ✅</br>in 9012.38 ns | ✅</br>in 1075.67 ns | ✅</br>in 29264.7 ns |
| x@x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x23456789.x23456789.x2345678</br>9.x23456789.x2</br> | Valid | ✅</br>in 56996.09 ns | ❌</br>in 38102.12 ns | ✅</br>in 8057.49 ns | ✅</br>in 36994.38 ns |
| 1234567890123456789012345678901234567890</br>123456789012345678@123456789012345678901</br>23456789012345678901234567890123456789.1</br>2345678901234567890123456789012345678901</br>234567890123456789.123456789012345678901</br>2345678901234567890123456789012345678901</br>23.test.org</br> | Valid | ✅</br>in 33695.59 ns | ✅</br>in 22076.15 ns | ✅</br>in 6450.96 ns | ✅</br>in 64570.6 ns |
| first.last@3com.com</br> | Valid | ✅</br>in 4297.41 ns | ✅</br>in 4142.51 ns | ✅</br>in 933.35 ns | ✅</br>in 20490.38 ns |
| first.last@123.test.org</br> | Valid | ✅</br>in 3763.21 ns | ✅</br>in 3109.65 ns | ✅</br>in 872.82 ns | ✅</br>in 14402.79 ns |
| first.last@x2345678901234567890123456789</br>0123456789012345678901234567890123.test.</br>org</br> | Valid | ✅</br>in 8954.87 ns | ✅</br>in 13191.22 ns | ✅</br>in 2839.16 ns | ✅</br>in 21993.08 ns |
| 1234567890123456789012345678901234567890</br>123456789012345678901234@test.org</br> | Valid | ✅</br>in 2480.45 ns | ✅</br>in 12115.98 ns | ✅</br>in 2109.13 ns | ✅</br>in 49070.08 ns |
| email@[123.123.123.123]</br> | Valid | ✅</br>in 8620.7 ns | ✅</br>in 3981.42 ns | ✅</br>in 674.57 ns | ✅</br>in 14499.59 ns |
| first.last@[12.34.56.78]</br> | Valid | ✅</br>in 8033.94 ns | ✅</br>in 4267.79 ns | ✅</br>in 918.69 ns | ✅</br>in 13520.95 ns |
| user+mailbox@test.org</br> | Valid | ✅</br>in 11753.14 ns | ✅</br>in 3938.06 ns | ✅</br>in 841.31 ns | ✅</br>in 16248.03 ns |
| customer/department=shipping@test.org</br> | Valid | ✅</br>in 32158.33 ns | ✅</br>in 7399.76 ns | ✅</br>in 1547.81 ns | ✅</br>in 25214.71 ns |
| $A12345@test.org</br> | Valid | ✅</br>in 10978.03 ns | ✅</br>in 3239.03 ns | ✅</br>in 710.12 ns | ✅</br>in 13712.09 ns |
| !def!xyz%abc@test.org</br> | Valid | ✅</br>in 7878.35 ns | ✅</br>in 5645.23 ns | ✅</br>in 1124.49 ns | ✅</br>in 17371.28 ns |
| _somename@test.org</br> | Valid | ✅</br>in 6832.46 ns | ✅</br>in 3043.88 ns | ✅</br>in 691.83 ns | ✅</br>in 11853.6 ns |
| dclo@us.ibm.com</br> | Valid | ✅</br>in 5976.23 ns | ✅</br>in 3299.23 ns | ✅</br>in 753.54 ns | ✅</br>in 8315.85 ns |
| test@xn--example.com</br> | Valid | ✅</br>in 5543.37 ns | ✅</br>in 4080.51 ns | ✅</br>in 904.73 ns | ✅</br>in 9925.63 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:7777:8888]</br> | Valid | ✅</br>in 4989.59 ns | ❌</br>in 5335.06 ns | ✅</br>in 1401.56 ns | ✅</br>in 18899.96 ns |
| first.last@[IPv6:1111:2222:3333::4444:55</br>55:6666:7777]</br> | Valid | ✅</br>in 7442.34 ns | ❌</br>in 8086.18 ns | ✅</br>in 1590.96 ns | ✅</br>in 22920.66 ns |
| first.last@[IPv6:::1111:2222:3333:4444:5</br>555:6666]</br> | Valid | ✅</br>in 4303.07 ns | ❌</br>in 5918.22 ns | ✅</br>in 1305.87 ns | ✅</br>in 17168.52 ns |
| first.last@[IPv6:1111:2222:3333::4444:55</br>55:6666]</br> | Valid | ✅</br>in 5743.6 ns | ❌</br>in 31113.8 ns | ✅</br>in 1433.95 ns | ✅</br>in 28991.12 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666::]</br> | Valid | ✅</br>in 6029.01 ns | ❌</br>in 27765.98 ns | ✅</br>in 1467.04 ns | ✅</br>in 14893.27 ns |
| first.last@[IPv6:::a2:a3:a4:b1:b2:b3:b4]</br> | Valid | ✅</br>in 4615.65 ns | ❌</br>in 3428.4 ns | ✅</br>in 978.9 ns | ✅</br>in 14932.21 ns |
| first.last@[IPv6:a1:a2:a3:a4:b1:b2:b3::]</br> | Valid | ✅</br>in 5154.28 ns | ❌</br>in 7419.11 ns | ✅</br>in 1108.1 ns | ✅</br>in 12826.79 ns |
| first.last@[IPv6:::]</br> | Valid | ✅</br>in 2130.09 ns | ❌</br>in 2727.85 ns | ✅</br>in 981.15 ns | ✅</br>in 15591.35 ns |
| first.last@[IPv6:::b4]</br> | Valid | ✅</br>in 3311.75 ns | ❌</br>in 2893.05 ns | ✅</br>in 800.36 ns | ✅</br>in 15725.44 ns |
| first.last@[IPv6:::b3:b4]</br> | Valid | ✅</br>in 4754.1 ns | ❌</br>in 2698.91 ns | ✅</br>in 1134.41 ns | ✅</br>in 12235.7 ns |
| first.last@[IPv6:a1::b4]</br> | Valid | ✅</br>in 4306.31 ns | ❌</br>in 22956.8 ns | ✅</br>in 608.52 ns | ✅</br>in 14069.32 ns |
| first.last@[IPv6:a1::]</br> | Valid | ✅</br>in 3236.1 ns | ❌</br>in 22943.78 ns | ✅</br>in 970.6 ns | ✅</br>in 10043.66 ns |
| first.last@[IPv6:a1:a2::]</br> | Valid | ✅</br>in 2747.69 ns | ❌</br>in 26162.75 ns | ✅</br>in 908.08 ns | ✅</br>in 14844.75 ns |
| first.last@[IPv6:0123:4567:89ab:cdef::]</br> | Valid | ✅</br>in 11062.33 ns | ❌</br>in 25468.9 ns | ✅</br>in 1134.4 ns | ✅</br>in 13893.69 ns |
| first.last@[IPv6:0123:4567:89ab:CDEF::]</br> | Valid | ✅</br>in 5664.0 ns | ❌</br>in 28176.52 ns | ✅</br>in 877.21 ns | ✅</br>in 14375.08 ns |
| first.last@[IPv6:::12.34.56.78]</br> | Valid | ✅</br>in 7521.0 ns | ❌</br>in 5046.78 ns | ✅</br>in 958.31 ns | ✅</br>in 13932.96 ns |
| first.last@[IPv6:1111:2222:3333::4444:12</br>.34.56.78]</br> | Valid | ✅</br>in 5798.17 ns | ❌</br>in 30143.61 ns | ✅</br>in 1196.5 ns | ✅</br>in 17821.11 ns |
| first.last@[IPv6:1111:2222:3333:4444:555</br>5:6666:12.34.56.78]</br> | Valid | ✅</br>in 10289.39 ns | ❌</br>in 40562.76 ns | ✅</br>in 1399.84 ns | ✅</br>in 15412.75 ns |
| first.last@[IPv6:1111:2222:3333::4444:55</br>55:12.34.56.78]</br> | Valid | ✅</br>in 4326.2 ns | ❌</br>in 31933.88 ns | ✅</br>in 1006.4 ns | ✅</br>in 13044.96 ns |
| aaa@[123.123.123.123]</br> | Valid | ✅</br>in 3844.93 ns | ✅</br>in 108093.92 ns | ✅</br>in 544.59 ns | ✅</br>in 6467.42 ns |
| first.last@[IPv6:::a3:a4:b1:ffff:11.22.3</br>3.44]</br> | Valid | ✅</br>in 6308.09 ns | ❌</br>in 5293.44 ns | ✅</br>in 1178.16 ns | ✅</br>in 25803.63 ns |
| first.last@[IPv6:::a2:a3:a4:b1:ffff:11.2</br>2.33.44]</br> | Valid | ✅</br>in 6818.1 ns | ❌</br>in 6668.92 ns | ✅</br>in 1417.48 ns | ✅</br>in 12870.41 ns |
| first.last@[IPv6:a1:a2:a3:a4::11.22.33.4</br>4]</br> | Valid | ✅</br>in 7259.19 ns | ❌</br>in 28623.63 ns | ✅</br>in 998.36 ns | ✅</br>in 11952.98 ns |
| first.last@[IPv6:a1:a2:a3:a4:b1::11.22.3</br>3.44]</br> | Valid | ✅</br>in 11004.15 ns | ❌</br>in 30881.94 ns | ✅</br>in 1242.75 ns | ✅</br>in 12139.3 ns |
| first.last@[IPv6:a1::11.22.33.44]</br> | Valid | ✅</br>in 5355.92 ns | ❌</br>in 28331.36 ns | ✅</br>in 905.23 ns | ✅</br>in 10400.95 ns |
| first.last@[IPv6:a1:a2::11.22.33.44]</br> | Valid | ✅</br>in 6500.44 ns | ❌</br>in 50851.86 ns | ✅</br>in 1226.04 ns | ✅</br>in 12490.93 ns |
| first.last@[IPv6:0123:4567:89ab:cdef::11</br>.22.33.44]</br> | Valid | ✅</br>in 6057.97 ns | ❌</br>in 27069.77 ns | ✅</br>in 1306.5 ns | ✅</br>in 11700.99 ns |
| first.last@[IPv6:0123:4567:89ab:CDEF::11</br>.22.33.44]</br> | Valid | ✅</br>in 5650.76 ns | ❌</br>in 33280.05 ns | ✅</br>in 1461.41 ns | ✅</br>in 22388.76 ns |
| first.last@[IPv6:a1::b2:11.22.33.44]</br> | Valid | ✅</br>in 4763.96 ns | ❌</br>in 26298.4 ns | ✅</br>in 951.96 ns | ✅</br>in 11545.18 ns |
| +@b.c</br> | Valid | ✅</br>in 1738.04 ns | ❌</br>in 2313.11 ns | ✅</br>in 300.24 ns | ✅</br>in 4100.64 ns |
| TEST@test.org</br> | Valid | ✅</br>in 4361.46 ns | ✅</br>in 2883.18 ns | ✅</br>in 541.45 ns | ✅</br>in 6596.85 ns |
| 1234567890@test.org</br> | Valid | ✅</br>in 3178.99 ns | ✅</br>in 2951.95 ns | ✅</br>in 607.49 ns | ✅</br>in 11194.34 ns |
| test-test@test.org</br> | Valid | ✅</br>in 4754.72 ns | ✅</br>in 4227.7 ns | ✅</br>in 733.52 ns | ✅</br>in 14342.43 ns |
| t*est@test.org</br> | Valid | ✅</br>in 10069.6 ns | ✅</br>in 4814.35 ns | ✅</br>in 659.51 ns | ✅</br>in 15652.97 ns |
| +1~1+@test.org</br> | Valid | ✅</br>in 4804.37 ns | ✅</br>in 3422.36 ns | ✅</br>in 796.16 ns | ✅</br>in 8617.66 ns |
| {_test_}@test.org</br> | Valid | ✅</br>in 5450.13 ns | ✅</br>in 3646.7 ns | ✅</br>in 470.68 ns | ✅</br>in 11673.41 ns |
| test@123.123.123.x123</br> | Valid | ✅</br>in 8619.47 ns | ❌</br>in 5753.1 ns | ✅</br>in 513.31 ns | ✅</br>in 12420.91 ns |
| cdburgess+!#$%&'*-/=?+_{}&#124;~test@gma</br>il.com</br> | Valid | ✅</br>in 8033.57 ns | ✅</br>in 7104.61 ns | ✅</br>in 942.75 ns | ✅</br>in 24880.75 ns |
| valid@about.museum</br> | Valid | ✅</br>in 3277.82 ns | ✅</br>in 2433.58 ns | ✅</br>in 312.33 ns | ✅</br>in 6833.99 ns |
| a@bar</br> | Valid | ✅</br>in 3043.6 ns | ✅</br>in 3422.54 ns | ✅</br>in 228.88 ns | ✅</br>in 5948.79 ns |
| a-b@bar.com</br> | Valid | ✅</br>in 3521.25 ns | ✅</br>in 2270.84 ns | ✅</br>in 293.99 ns | ✅</br>in 5960.32 ns |
| +@b.com</br> | Valid | ✅</br>in 2757.24 ns | ✅</br>in 1478.36 ns | ✅</br>in 185.38 ns | ✅</br>in 4972.45 ns |
| cal(foo\@bar)@iamcal.com</br> | Valid | ✅</br>in 6019.63 ns | ❌</br>in 1601.93 ns | ❌</br>in 18764.7 ns | ✅</br>in 26792.24 ns |
| (comment)test@test.org</br> | Valid | ✅</br>in 5932.16 ns | ❌</br>in 1411.33 ns | ✅</br>in 310.99 ns | ✅</br>in 25316.76 ns |
| (foo)cal(bar)@(baz)iamcal.com(quux)</br> | Valid | ✅</br>in 7582.82 ns | ❌</br>in 2057.17 ns | ❌</br>in 18931.33 ns | ✅</br>in 34465.38 ns |
| cal(foo\)bar)@iamcal.com</br> | Valid | ✅</br>in 5731.14 ns | ❌</br>in 2167.38 ns | ❌</br>in 19294.35 ns | ✅</br>in 19781.44 ns |
| cal(woo(yay)hoopla)@iamcal.com</br> | Valid | ✅</br>in 3959.2 ns | ❌</br>in 1391.68 ns | ❌</br>in 18539.68 ns | ❌</br>in 17363.1 ns |
| first(Welcome to&#13;&#10; the ("wonderf</br>ul" (!)) world&#13;&#10; of email)@test.</br>org</br> | Valid | ✅</br>in 6538.77 ns | ❌</br>in 2807.71 ns | ❌</br>in 19828.31 ns | ❌</br>in 49631.64 ns |
| pete(his account)@silly.test(his host)</br> | Valid | ✅</br>in 5777.59 ns | ❌</br>in 2179.83 ns | ❌</br>in 22062.1 ns | ✅</br>in 45828.58 ns |
| first(abc\(def)@test.org</br> | Valid | ✅</br>in 6444.53 ns | ❌</br>in 1941.53 ns | ❌</br>in 23326.28 ns | ✅</br>in 26759.53 ns |
| a(a(b(c)d(e(f))g)h(i)j)@test.org</br> | Valid | ✅</br>in 10159.96 ns | ❌</br>in 1574.91 ns | ❌</br>in 27470.42 ns | ❌</br>in 6527.21 ns |
| c@(Chris's host.)public.example</br> | Valid | ✅</br>in 6217.64 ns | ❌</br>in 1547.25 ns | ❌</br>in 18667.83 ns | ✅</br>in 9375.99 ns |
| a@b.co-foo.uk</br> | Valid | ✅</br>in 5583.3 ns | ✅</br>in 4346.75 ns | ✅</br>in 356.43 ns | ✅</br>in 7671.72 ns |
| _Yosemite.Sam@test.org</br> | Valid | ✅</br>in 6450.21 ns | ✅</br>in 6147.0 ns | ✅</br>in 489.08 ns | ✅</br>in 20066.26 ns |
| ~@test.org</br> | Valid | ✅</br>in 4035.21 ns | ✅</br>in 2186.79 ns | ✅</br>in 250.64 ns | ✅</br>in 5975.73 ns |
| Iinsist@(that comments are allowed)this.</br>is.ok</br> | Valid | ✅</br>in 8297.91 ns | ❌</br>in 2471.57 ns | ❌</br>in 23365.43 ns | ✅</br>in 39141.96 ns |
| test@Bücher.ch</br> | Valid | ✅</br>in 38942.32 ns | ✅</br>in 27549.22 ns | ✅</br>in 269.82 ns | ❌</br>in 5913.98 ns |
| あいうえお@example.com</br> | Valid | ✅</br>in 5076.25 ns | ✅</br>in 9883.36 ns | ✅</br>in 481.66 ns | ❌</br>in 5809.42 ns |
| Pelé@example.com</br> | Valid | ✅</br>in 4265.27 ns | ✅</br>in 5122.71 ns | ✅</br>in 411.17 ns | ❌</br>in 13329.83 ns |
| δοκιμή@παράδειγμα.δοκιμή</br> | Valid | ✅</br>in 48013.48 ns | ❌</br>in 79289.92 ns | ✅</br>in 8019.94 ns | ❌</br>in 1559.79 ns |
| 我買@屋企.香港</br> | Valid | ✅</br>in 6038.51 ns | ✅</br>in 7268.29 ns | ✅</br>in 2130.51 ns | ❌</br>in 1974.5 ns |
| 二ノ宮@黒川.日本</br> | Valid | ✅</br>in 10492.06 ns | ❌</br>in 27170.58 ns | ✅</br>in 3959.0 ns | ❌</br>in 2475.97 ns |
| медведь@с-балалайкой.рф</br> | Valid | ✅</br>in 14478.31 ns | ✅</br>in 18564.2 ns | ✅</br>in 6625.5 ns | ❌</br>in 2281.54 ns |
| संपर्क@डाटामेल.भारत</br> | Valid | ✅</br>in 15624.5 ns | ✅</br>in 25235.36 ns | ❌</br>in 23689.99 ns | ❌</br>in 2622.46 ns |
| email@example.com (Joe Smith)</br> | Valid | ✅</br>in 7422.87 ns | ❌</br>in 2181.61 ns | ✅</br>in 6979.76 ns | ✅</br>in 13573.48 ns |
| cal@iamcal(woo).(yay)com</br> | Valid | ✅</br>in 9008.08 ns | ❌</br>in 8574.57 ns | ✅</br>in 4637.93 ns | ❌</br>in 9797.91 ns |
| first(abc.def).last@test.org</br> | Valid | ✅</br>in 3697.48 ns | ❌</br>in 2630.56 ns | ❌</br>in 12898.13 ns | ❌</br>in 22352.1 ns |
| first(a"bc.def).last@test.org</br> | Valid | ✅</br>in 6965.41 ns | ❌</br>in 4856.17 ns | ❌</br>in 24225.97 ns | ❌</br>in 25121.85 ns |
| first.(")middle.last(")@test.org</br> | Valid | ✅</br>in 7314.5 ns | ❌</br>in 2765.67 ns | ❌</br>in 14770.8 ns | ❌</br>in 12057.83 ns |
| first.last@x(123456789012345678901234567</br>8901234567890123456789012345678901234567</br>890).com</br> | Valid | ✅</br>in 5982.56 ns | ❌</br>in 31317.74 ns | ✅</br>in 4920.9 ns | ❌</br>in 48204.17 ns |
| user%uucp!path@berkeley.edu</br> | Valid | ✅</br>in 7017.95 ns | ✅</br>in 10785.11 ns | ✅</br>in 9190.75 ns | ✅</br>in 11194.02 ns |
| first().last@test.org</br> | Valid | ✅</br>in 4319.38 ns | ❌</br>in 3262.95 ns | ❌</br>in 19526.58 ns | ❌</br>in 10180.02 ns |
| Totals | 311/311 | 311/311</br>Average Time: 5602.948360128617 ns | 237/311</br>Average Time: 9227.577266881028 ns | 211/311</br>Average Time: 9624.800160771705 ns | 222/311</br>Average Time: 58096.01639871383 ns |
