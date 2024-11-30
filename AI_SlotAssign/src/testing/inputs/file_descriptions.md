# test_input_01.txt
### Description:
Expands on example from the proposal, adding games and slots that will need to be checked for city hard constraints. 
### Parameters 
w_minfilled, w_pref, w_pair, w_secdiff, pen_gamemin, pen_practicemin, pen_notpaired, pen_section:
1, 0, 0, 0, 1, 0, 0, 0  
### Result
There would be 4 schedules with an eval of 0, rest would be 1 (see test_input_walkthrough)
(GS_1M, GS_1T, GS_1W, GS_1R, GS_2R, PS_1M, PS_1W, PS_1R, PS_2R) =
1. ({g2,g4}, {}, {g2,g4}, {g1,g5}, {g3}, {}, {p1, p6}, {}, {})
2. ({g2,g5}, {}, {g2,g5}, {g1}, {g3,g4}, {}, {p1, p6}, {}. {})
3. ({g4,g5}, {}, {g4,g5}, {g1}, {g2, g3}, {}, {p1,p6}, {}, {})
4. ({g2,g4}, {}, {g2,g4}, {g5}, {g1,g3}, {}, {p1,p6}, {}, {})
If picking left to right it would pick the first one.
