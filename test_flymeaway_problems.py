'''
        This code is modified from UDACITY Artificial Inteligent NanoDegree class planning aircargo project.
        UDACITY aimacode library is used for planning, search and utility classes. libraries also included in github location.
        I have modified the code to apply for my flight problems and to generate my own planning graph.
        https://classroom.udacity.com/nanodegrees/nd898/dashboard/overview
        '''


import os
import sys
parent = os.path.dirname(os.path.realpath(__file__))
sys.path.append(os.path.join(os.path.dirname(parent), "aimacode"))
from aimacode.planning import Action
from aimacode.utils import expr
from aimacode.search import Node
import unittest
from lp_utils import decode_state
from flyMeAway_problems import (
    flymeaway_p1, flymeaway_p2
)

class TestFlighProb1(unittest.TestCase):

    def setUp(self):
        self.p1 = flymeaway_p1()

    def test_ACP1_num_fluents(self):
        self.assertEqual(len(self.p1.initial), 12)

    def test_ACP1_num_requirements(self):
        self.assertEqual(len(self.p1.goal),2)


class TestFlighProb2(unittest.TestCase):

    def setUp(self):
        self.p2 = flymeaway_p2()

    def test_ACP2_num_fluents(self):
        self.assertEqual(len(self.p2.initial), 27)

    def test_ACP2_num_requirements(self):
        self.assertEqual(len(self.p2.goal),3)


class TestFlighMethods(unittest.TestCase):

    def setUp(self):
        self.p1 = flymeaway_p1()
        self.act1 = Action(
            expr('Load(P, AL, BOS)'),
            [[expr('At(P, BOS)'), expr('At(AL, BOS)')], []],
            [[expr('In(P, AL)')], [expr('At(P, BOS)')]]
        )

    def test_AC_get_actions(self):
        # to see a list of the actions, uncomment below
        # print("\nactions for problem")
        # for action in self.p1.actions_list:
        #     print("{}{}".format(action.name, action.args))
        self.assertEqual(len(self.p1.actions_list), 20)

    def test_AC_actions(self):
        # to see list of possible actions, uncomment below
        # print("\npossible actions:")
        # for action in self.p1.actions(self.p1.initial):
        #     print("{}{}".format(action.name, action.args))
        self.assertEqual(len(self.p1.actions(self.p1.initial)), 4)

    def test_AC_result(self):
        fs = decode_state(self.p1.result(self.p1.initial, self.act1), self.p1.state_map)
        self.assertTrue(expr('In(P, AL)') in fs.pos)
        self.assertTrue(expr('At(P, BOS)') in fs.neg)

    def test_h_ignore_preconditions(self):
        n = Node(self.p1.initial)
        self.assertEqual(self.p1.h_ignore_preconditions(n),2)

if __name__ == '__main__':
    unittest.main()
