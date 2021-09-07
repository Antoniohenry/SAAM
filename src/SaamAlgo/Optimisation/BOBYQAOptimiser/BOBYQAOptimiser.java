package SaamAlgo.Optimisation.BOBYQAOptimiser;

import SaamAlgo.QL;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;

public class BOBYQAOptimiser {

    static String filePath = "src\\\\SaamAlgo\\\\Optimisation\\\\BOBYQAOptimiser\\\\BOBY";


    private final MultivariateFunction objective = new MultivariateFunction() {
        @Override
        public double value(double[] point) {

            int maxi = 3;
            double sum = 0;
            for(int i = 0; i < maxi; i++) {

                QL ql = new QL(point[0] * 500, 1, 0.995, (int) (point[1] * 30), 0.7, (int) (point[2] *(-100)), point[3] * 0.99, point[4] * 0.3, 0.3, 1, 60, filePath);

                sum += ql.totalReward;

            }
            return sum/maxi;
        }
    };

    private final double[][] boundaries = {{0.2, 0.66, 0.66, 0.9, 0.5}, {2., 2.33, 1.5, 1.009, 1.5}};

    private final double[] start = {1., 1., 1., 1., 1.};

    private final GoalType goalType = GoalType.MAXIMIZE;

    public BOBYQAOptimiser() {

        BOBYQAOptimizer optim = new BOBYQAOptimizer( 20, 1000, 1E-8);
        PointValuePair pointValuePair = optim.optimize(new MaxEval(100), new ObjectiveFunction(objective), goalType, new InitialGuess(start), new SimpleBounds(boundaries[0], boundaries[1]));

        System.out.println("optim = " + optim);

        System.out.println("pointValuePair = " + pointValuePair.getPoint() + " " + pointValuePair.getValue());

    }
}
