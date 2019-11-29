def transformMap1 = [
  desc:"take input from 1 to 10 and multiply it with 4",
  axes: ["val1"],
  headers:[
    val1:[1,2,3,4,5,6,7,8,9,10]
  ],
  values:[4,8,12,16,20,24,28,32,36,40],
]

def transformMap2 = [
  desc:"take inputs from 0 to 100 and substract 2, bun only in positive range",
  axes: ["map1"], // takes result values from map1
  headers:[
    map1:[0,10,20,30,40,50,60,70,80,90,100]
  ],
  values:[0,8,18,28,38,48,58,68,78,88,98],
]

println "let's begin"
def proc = new Processor()
proc.inputs = ["val1"] // input parameters.
proc.maps = [
  "map1":transformMap1,
  "map2":transformMap2,
]
proc.outputs = [
  res:[source:"map2"],
]

// test 1  -simple
proc.set_input("val1", 4) // 1st map transforms 4 to 16, 2nd map transforms 16 to 10, and subsctacts 2 from it, so result must be 8
proc.process()
println "got result for 4: " + proc.get_result("res")
assert proc.get_result("res") == 8

// test 2 - simple
proc.set_input("val1", 7) // 1st map transforms 7 to 28, 2nd map transforms 28 to 20, and subsctacts 2 from it, so result must be 18
proc.process()
println "got result for 7: " + proc.get_result("res")
assert proc.get_result("res") == 18

// test 3 - simple
proc.set_input("val1", 5) // 1st map transforms 5 to 20, 2nd map transforms 20 to 20, and subsctacts 2 from it, so result must be 18
proc.process()
println "got result for 5: " + proc.get_result("res")
assert proc.get_result("res") == 18

// test 4 - out of range from top
proc.set_input("val1", 12) // overflow range from top, so 12 turns to 10, transforms to 40, then to 38
proc.process()
println "got result for 12: " + proc.get_result("res")
assert proc.get_result("res") == 38

// test 5 - out of range from bottom
proc.set_input("val1", -12) // overflow range from bottom, so -12 turns to 1, transforms to 4, second map rounds 4 to 0 and maps 0  to 0
proc.process()
println "got result for -12: " + proc.get_result("res")
assert proc.get_result("res") == 0
