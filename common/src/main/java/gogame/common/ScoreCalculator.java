package gogame.common;

// From http://www.cs.cmu.edu/~wjh/go/rules/Japanese.html
// Article 8. Territory
// Empty points surrounded by the live stones of just one player are called "eye points."
// Other empty points are called "dame."
// Stones which are alive but possess dame are said to be in "seki."
// Eye points surrounded by stones that are alive but not in seki are called "territory," each eye point counting as one point of territory.

public class ScoreCalculator {
    /**
     * Calculates territory and final score based on proposing of life and death
     *
     * @param aliveness only stones and alive fields are set
     * @return Scoring where also territories and winner are set
     */
    public static Scoring calculate(Scoring aliveness){
        //todo: implement according to rules
        return null;
    }
}
