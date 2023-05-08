# McMohans-Burgers---Restaurant-Orders-Simulation


<b>Objectives:</b><br />
• Implement a simulation environment for McMahon's Burgers.<br />
• Track statistics such as average waiting time and queue length to identify areas for improvement.<br />
• Simulate customer arrivals, order placements, billing, food preparation, and order deliveries.<br />
• Efficiently handle multiple billing counters, random customer arrivals, and simultaneous events.<br />
• Use proper data structures, including heaps and queues, to optimize the simulation's time and memory complexity.<br />
• Provide functions to retrieve customer and griddle states, calculate customer wait times, and calculate average wait time per customer.

<b>Constraints:</b><br />
• The restaurant has K billing counters numbered from 1 to K.<br />
• The griddle can cook at most M burger patties simultaneously.<br />
• Customers arrive randomly and join the billing queue with the smallest length at that time.<br />
• Each billing specialist takes k units of time (where k is the counter number) to complete the order.<br />
• The chef prepares burgers in the sequence they receive the orders, prioritizing orders from higher numbered billing queues when multiple orders arrive simultaneously.<br />
• Each burger patty takes exactly 10 units of time to cook.<br />
• The simulation is event-driven, where events include customer arrivals, order completions, burger cooking, and burger deliveries.<br />
• The simulation maintains customer states (waiting in queue, waiting for food, or left the building) and a global clock to move events forward.<br />

<b>Major Complexity Factors:</b><br />
• Efficiently handling multiple billing counters and selecting the shortest queue.<br />
• Simultaneous events and their prioritization based on the specific order.<br />
• Efficient management of the griddle, including tracking cooked and waiting burgers.<br />
• Tracking customer states, their wait times, and calculating average wait time.<br />
• Proper implementation of heaps and queues to optimize the time and memory complexity of the simulation.<br />
