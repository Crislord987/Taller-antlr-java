import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends LabeledExprBaseVisitor<Double> {
    Map<String, Double> memory = new HashMap<>();

    @Override
    public Double visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        double value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    @Override
    public Double visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Double value = visit(ctx.expr());
        if (!Double.isNaN(value)) {
            System.out.println(value);
        }
        return 0.0;
    }

    @Override
    public Double visitFloat(LabeledExprParser.FloatContext ctx) {
        return Double.valueOf(ctx.FLOAT().getText());
    }

    @Override
    public Double visitId(LabeledExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if (!memory.containsKey(id)) {
            System.err.println("Error: Variable '" + id + "' no definida");
            return Double.NaN;
        }
        return memory.get(id);
    }

    @Override
    public Double visitNegative(LabeledExprParser.NegativeContext ctx) {
        return -visit(ctx.expr());
    }

    @Override
    public Double visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));

        if (ctx.op.getType() == LabeledExprParser.DIV) {
            if (right == 0) {
                System.err.println("Error: División por cero");
                return Double.NaN;
            }
            return left / right;
        }
        return left * right;
    }

    @Override
    public Double visitAddSub(LabeledExprParser.AddSubContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        return ctx.op.getType() == LabeledExprParser.ADD ? left + right : left - right;
    }

    @Override
    public Double visitPower(LabeledExprParser.PowerContext ctx) {
        double base = visit(ctx.expr(0));
        double exponent = visit(ctx.expr(1));

        if (base == 0 && exponent == 0) {
            System.err.println("Error: Indefinido (0^0)");
            return Double.NaN;
        }
        return Math.pow(base, exponent);
    }

    @Override
    public Double visitSqrt(LabeledExprParser.SqrtContext ctx) {
        double value = visit(ctx.expr());
        if (value < 0) {
            System.err.println("Error: Raíz de número negativo");
            return Double.NaN;
        }
        return Math.sqrt(value);
    }

    @Override
    public Double visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}

