
// one dimensional map
def map1 = [
  desc:"temp gauge needle gegrees",
  axes: ["temp"],
  headers:[
    temp:[0,10,20,30,40]
  ],
  values:[-40,-20,0,20,40],
]

// two-dimensions map
def twodimMap = [
  desc:"multiply two values",
  axes: ["val1","val2"],
  headers:[
    val1:[0,1,2,3,4], // horizontal axis
    val2:[0,1,2,3,4], // vertical axis
  ],
  values:[  // actually it is 1-dimension array
    0, 0, 0, 0, 0,
    0, 1, 2, 3, 4,
    0, 2, 4, 6, 8,
    0, 3, 6, 9,12,
    0, 4, 8,12,16,
  ]
]

// three-dimensions map
def threedimMap = [
  desc:"cubic map",
  axes: ["x","y","z"],
  headers:[
    x:[0,1,2], // x axis
    y:[0,1,2], // y axis
    z:[0,1,2], // z axis
  ],
  values:[  // actually it is 1-dimension array
    0, 0, 0, // z = 0
    0, 0, 0,
    0, 0, 0,

    0, 0, 0, // z = 1
    0, 1, 2,
    0, 2, 4,

    0, 0, 0, // z = 2
    0, 2, 4,
    0, 4, 8,
  ]
]

println "let's begin"
def proc = new Processor()
proc.inputs = ["temp","val1","val2","x","y","z"] // input parameters.
proc.maps = [
  "needle_degree":map1,
  "multiply":twodimMap,
  "cube":threedimMap
]
proc.outputs = [
  needle:[source:"needle_degree"],
  multiply_result:[source:"multiply"],
  cube_res:[source:"cube"]
]

// all input paramerters must be set
proc.set_input("temp", 23)
proc.set_input("val1", 0)
proc.set_input("val2", 0)
proc.set_input("x", 0)
proc.set_input("y", 0)
proc.set_input("z", 0)

// multiply test
for(int i = 0; i <= 4; i++){
  for(int j = 0; j <= 4; j++) {
    proc.set_input("val1", i)
    proc.set_input("val2", j)
    proc.process()
    println i+"*"+j+" = "+proc.get_result("multiply_result")
    assert proc.get_result("multiply_result") == i*j
  }
}
// cube test
for(int x = 0; x <= 2; x++){
  for(int y = 0; y <= 2; y++) {
    for(int z = 0; z <= 2; z++) {
      proc.set_input("x", x)
      proc.set_input("y", y)
      proc.set_input("z", z)
      proc.process()
      println x+"*"+y+"*"+z+" = "+proc.get_result("cube_res")
      assert proc.get_result("cube_res") == x*y*z
    }
  }
}
// 1-dimenson map test
println "needle degree for temp 23: "+proc.get_result("needle")+" degree"
