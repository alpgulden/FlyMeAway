'''
        This code is modified from UDACITY Artificial Inteligent NanoDegree class planning aircargo project.
        UDACITY aimacode library is used for planning, search and utility classes. libraries also included in github location.
        I have modified the code to apply for my flight problems and to generate my own planning graph.
        https://classroom.udacity.com/nanodegrees/nd898/dashboard/overview
'''



from aimacode.logic import PropKB
from aimacode.planning import Action
from aimacode.search import (
    Node, Problem,
)
from aimacode.utils import expr
from lp_utils import (
    FluentState, encode_state, decode_state,
)
from my_planning_graph import PlanningGraph


class FlymeawayProblem(Problem):
    def __init__(self, passenger, planes, airports, initial: FluentState, goal: list):
        """

        :param passenger: str
            passenger in the problem
        :param planes: list of str
            planes in the problem
        :param airports: list of str
            airports in the problem
        :param initial: FluentState object
            positive and negative literal fluents (as expr) describing initial state
        :param goal: list of expr
            literal fluents required for goal test
        """
        self.state_map = initial.pos + initial.neg
        self.initial_state_TF = encode_state(initial, self.state_map)
        Problem.__init__(self, self.initial_state_TF, goal=goal)
        self.passenger = passenger
        self.planes = planes
        self.airports = airports
        self.actions_list = self.get_actions()

    def get_actions(self):
        '''
        This method creates concrete actions (no variables) for all actions in the problem
        domain action schema and turns them into complete Action objects as defined in the
        aimacode.planning module. It is computationally expensive to call this method directly;
        however, it is called in the constructor and the results cached in the `actions_list` property.

        Returns:
        ----------
        list<Action>
            list of Action objects
        '''

        def GetOn_actions():
            '''Create all concrete GetOn actions and return a list

            :return: list of Action objects
            '''
            loads = []
            #for only one passenger
            for plane in self.planes: #we have plan for one airline but code include multiple
                for airport in self.airports:
                    precond_pos = [
                        expr("At({}, {})".format(passenger, airport)),
                        expr("At({}, {})".format(plane, airport))
                    ]
                    precond_neg = []
                    effect_add = [expr("In({}, {})".format(passenger, plane))]
                    effect_rem = [expr("At({}, {})".format(passenger, airport))]
                    load = Action(
                        expr("Load({}, {}, {})".format(passenger, plane, airport)),
                        [precond_pos, precond_neg],
                        [effect_add, effect_rem]
                        )
                    loads.append(load)  
            return loads

        def GetOff_actions():
            '''Create all concrete GetOff actions and return a list

            :return: list of Action objects
            '''
            unloads = []
            #for only one passenger
            for plane in self.planes: #we have plan for one airline but code include multiple
                for airport in self.airports:
                    precond_pos = [
                        expr("In({}, {})".format(passenger, plane)),
                        expr("At({}, {})".format(plane, airport))
                    ]
                    precond_neg = []
                    effect_add = [expr("At({}, {})".format(passenger, airport))]
                    effect_rem = [expr("In({}, {})".format(passenger, plane))]
                    unload = Action(
                        expr("Unload({}, {}, {})".format(passenger, plane, airport)),
                        [precond_pos, precond_neg],
                        [effect_add, effect_rem]
                    )
                    unloads.append(unload)
            return unloads

        def Fly_actions():
            '''Create all concrete Fly actions and return a list

            :return: list of Action objects
            '''
            flys = []
            for fr in self.airports:
                for to in self.airports:
                    if fr != to:
                        for p in self.planes: #we have plan for one airline but code include multiple
                            precond_pos = [expr("At({}, {})".format(p, fr)),
                                           ]
                            precond_neg = []
                            effect_add = [expr("At({}, {})".format(p, to))]
                            effect_rem = [expr("At({}, {})".format(p, fr))]
                            fly = Action(expr("Fly({}, {}, {})".format(p, fr, to)),
                                         [precond_pos, precond_neg],
                                         [effect_add, effect_rem])
                            flys.append(fly)
            return flys

        return GetOn_actions() + GetOff_actions() + Fly_actions()

    def actions(self, state: str) -> list:
        """ Return the actions that can be executed in the given state.

        :param state: str
            state represented as T/F string of mapped fluents (state variables)
            e.g. 'FTTTFF'
        :return: list of Action objects
        """
        kb = PropKB()
        kb.tell(decode_state(state, self.state_map).pos_sentence())
        possible_actions = []
        for action in self.actions_list:

            # Assume action is possible
            is_action_possible = True

            for c in action.precond_neg:
                if c in kb.clauses:
                    is_action_possible = False
                    break # No need to keep searching

            # Only check if action is still possible
            if is_action_possible:
                for c in action.precond_pos:
                    if c not in kb.clauses:
                        is_action_possible = False
                        break # No need to keep searching

            if is_action_possible: possible_actions.append(action)

        return possible_actions

    def result(self, state: str, action: Action):
        """ Return the state that results from executing the given
        action in the given state. The action must be one of
        self.actions(state).

        :param state: state entering node
        :param action: Action applied
        :return: resulting state after action
        """
        prev_state = decode_state(state, self.state_map)
        next_state = FluentState([], [])

        for fluent in prev_state.pos:
            if fluent not in action.effect_rem:
                next_state.pos.append(fluent)
        for fluent in prev_state.neg:
            if fluent not in action.effect_add:
                next_state.neg.append(fluent)

        for fluent in action.effect_add:
            if fluent not in next_state.pos:
                next_state.pos.append(fluent)
        for fluent in action.effect_rem:
            if fluent not in next_state.neg:
                next_state.neg.append(fluent)

        return encode_state(next_state, self.state_map)

    def goal_test(self, state: str) -> bool:
        """ Test the state to see if goal is reached

        :param state: str representing state
        :return: bool
        """
        kb = PropKB()
        kb.tell(decode_state(state, self.state_map).pos_sentence())
        for c in self.goal:
            if c not in kb.clauses:
                return False
        return True

    def h_1(self, node: Node):
        # note that this is not a true heuristic
        h_const = 1
        return h_const

    def h_pg_levelsum(self, node: Node):
        '''
        This heuristic uses a planning graph representation of the problem
        state space to estimate the sum of all actions that must be carried
        out from the current state in order to satisfy each individual goal
        condition.
        '''
        # requires implemented PlanningGraph class
        pg = PlanningGraph(self, node.state)
        pg_levelsum = pg.h_levelsum()
        return pg_levelsum

    def h_ignore_preconditions(self, node: Node):
        '''
        This heuristic estimates the minimum number of actions that must be
        carried out from the current state in order to satisfy all of the goal
        conditions by ignoring the preconditions required for an action to be
        executed.
        '''
        kb = PropKB()
        kb.tell(decode_state(node.state, self.state_map).pos_sentence())
        kb_clauses = kb.clauses
        actions_count = 0
        for clause in self.goal:
            if clause not in kb_clauses:
                actions_count += 1
        return actions_count


def flymeaway_p1() -> FlymeawayProblem:
    # planning for no transfer for passenger from BOS to IST with THY (turkish airline)
    passenger = ['P']  # 1 passenger
    planes = ['THY']    # 1 airline. THY 
    airports = ['IST', 'BOS']
    pos = [
        expr('At(P, BOS)'),
        expr('At(THY, BOS)'),
    ]
    neg = [
        expr('At(P, IST)'),
        expr('In(P, THY)'),
        expr('At(THY, IST)')
    ]
    init = FluentState(pos, neg)
    goal = [
        expr('At(P, IST)')
    ]
    return AirCargoProblem(cargos, planes, airports, init, goal)


def flymeaway_p2() -> FlymeawayProblem:
    # planning for one transfer for passenger from BOS to IST with THY (turkish airline)
    passenger = ['P']
    planes = ['THY']
    airports = ['BOS','PRS','FRK','MIL']
    pos = [
        expr('At(P, BOS)'),
        expr('At(AL1, BOS)'),
        expr('At(AL2, PRS)'),
        expr('At(AL2, FRK)'),
        expr('At(AL3, MIL)')
    ]
    neg = [
        expr('At(P, IST)'),
        expr('At(P, PRS)'),
        expr('At(P, FRK)'),
        expr('At(P, MIL)'),
        expr('In(P, THY)'),
        expr('At(THY, IST)'),
        expr('At(THY, PRS)'),
        expr('At(THY, FRK)'),
        expr('At(THY, MIL)')
    ]
    init = FluentState(pos, neg)
    goal = [
        expr('At(P, IST)')
    ]
    return AirCargoProblem(cargos, planes, airports, init, goal)

