class Processor {

  public inputs = []
  public outputs = []
  public maps = [:]

  private inputs_temp = [:]
  private calculated_maps = [:]
  private outputs_temp = [:]

  public set_input(name, value){
    if(this.inputs.contains(name)){
      inputs_temp[name] = value
      // println "input "+name+" set to: "+value
    }
  }

  public get_output(){
    return this.outputs_temp
  }

  public get_result(param){
    return this.outputs_temp[param]
  }

  public process(){
    this.calculated_maps = [:]
    outputs.each {
      // println "processing "+it.key
      def map_name = it.value.source
      // println "using map: "+map_name
      if(this.maps.containsKey(map_name)){
        // println "map exists, processing..."
        def map = this.maps[map_name]
        // println "map "+it.value.source+" has "+map.axes.size()+" dimensions"
        def val = this.process_map(map_name)
        this.outputs_temp[it.key] = val
      } else {
        println "map does not exists, fail"
      }
    }
  }

  private process_map(map_name){
    if(this.calculated_maps.containsKey(map_name)){
      return this.calculated_maps[map_name]
    }
    // println "checking in inputs..."
    if(inputs.contains(map_name)){
      return inputs_temp[map_name]
    }
    // println "calculating..."
    def map = this.maps[map_name]

    def inputs = []
    def lows = []
    def highs = []
    def mins = []
    def maxs = []

    def axis_pos = 0 // bottom range in all dimensions
    def axis_next_pos = 0
    def dim_multiplier = 1
    map.axes.eachWithIndex {axis, dimension ->
      // println "processing axis: "+axis
      def input = process_map(axis) // recursive find input value
      inputs << input
      // println "got input value for axis "+axis+": "+input
      def input_ranges = map.headers[axis]
      // println "input ranges: "+input_ranges
      if(dimension != 0){
        dim_multiplier *= input_ranges.size()
      }
      def maxrange = input_ranges.size() -1
      def apos = 0 // value index in current dimension
      def apos_next = 0 // value index for next value in current dimension
      for(int i = 0; i < input_ranges.size(); i++){
        def j = i+1
        if(j <= maxrange){
          if(input_ranges[i] <= input && input_ranges[j] > input){
            apos = i
            apos_next = j
            lows << input_ranges[i]
            highs << input_ranges[j]
            break
          }
        } else { // last range
          if(input >= input_ranges[maxrange]){
            apos = maxrange
            apos_next = maxrange
            lows << input_ranges[maxrange]
            highs << input_ranges[maxrange]
          }
        }
      }
      // println "got position in axis "+axis+": "+apos
      def map_offset = apos * dim_multiplier
      axis_pos += map_offset
      mins <<  map.values[axis_pos]
      def map_next_offset = apos_next * dim_multiplier
      axis_next_pos += map_next_offset
      maxs << map.values[axis_next_pos]
      // println "additional map offset: "+map_offset

    }
    // println "resulting value offset: " + axis_pos

    def val = map.values[axis_pos] // by "exact maps this is end value"
    if(map.type == "interpolated"){
      println "got interpolation map"
      println inputs
      println lows
      println highs
      println mins
      println maxs
      inputs.eachWithIndex {inp, idx ->
        // calc vector for this dimension
      }
    }

    this.calculated_maps[map_name] = val // cache it
    return val
  }

}
