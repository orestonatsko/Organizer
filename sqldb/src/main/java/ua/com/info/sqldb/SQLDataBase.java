package ua.com.info.sqldb;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import net.sourceforge.jtds.jdbc.JtdsResultSet;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.info.data.Column;
import ua.com.info.data.Command;
import ua.com.info.data.Data;
import ua.com.info.data.DataBase;
import ua.com.info.data.Direction;
import ua.com.info.data.IDataBase;
import ua.com.info.data.IDataObject;
import ua.com.info.data.Parameter;
import ua.com.info.data.Parameters;
import ua.com.info.data.Result;
import ua.com.info.data.Row;
import ua.com.info.data.Status;
import ua.com.info.data.Table;
import ua.com.info.data.Variable;
import ua.com.info.data.Variables;

import static ua.com.info.data.ResultKt.error;

/**
 * Створено v.m 27.07.2017.
 */


public class SQLDataBase implements IDataBase {

    private final String TAG = "SQL";
    private static Connection connection;
    private String ConnectionURL;

    public SQLDataBase(String server, String instance, String database, String user, String password) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + (instance == null ? "" : ";instance=" + instance) + ";DatabaseName="
                    + database + ";user=" + user + ";password=" + password + ";loginTimeout=2";//todo + ";ssl=require";
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Log.d(TAG, e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void openConnection() {
        try {
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Execute(String cmd, Parameters parameters, Listener listener) {
        ExecuteAsync task = new ExecuteAsync();
        try {
            task.execute(new SQLCommand(cmd, parameters, listener));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onExecuted(new Result(Status.Error, e.toString()));
        }
    }

    @Override
    public void getTable(String cmd, Parameters parameters, Listener listener) {
        GetTableAsync task = new GetTableAsync();
        try {
            task.execute(new SQLCommand(cmd, parameters, listener));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onExecuted(new Result(Status.Error, e.getMessage()));
        }
    }

    @Override
    public Result getTable(String cmd, Parameters parameters) {
        GetTableAsync task = new GetTableAsync();
        try {
            return task.execute(new SQLCommand(cmd, parameters)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void getData(String cmd, Parameters parameters, Listener listener) {
        GetDataAsync task = new GetDataAsync();
        try {
            task.execute(new SQLCommand(cmd, parameters, listener));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onExecuted(new Result(Status.Error, e.getMessage()));
        }
    }

    @Override
    public void Batch(Data batch, Listener listener) {
//         batch.createGlobalParameters();
        //   SqlTransaction tran = null;
        try {
            for (IDataObject o : batch) {
                if (o instanceof Command) {
                    Command cmd = (Command) o;
//                    if (p.refId != 0)
//                        p.value = batch.global[p.refId].value;
                    ExecuteAsync task = new ExecuteAsync();
                    task.execute(new SQLCommand(cmd.command, cmd.parameters, listener));
                }
            }

      /*      using(SqlConnection conn = new SqlConnection(SQL_conn))
            {
                conn.Open();
                зєднанняВстановлено = true;
                tran = conn.BeginTransaction();
                Data ret = new Data();
                foreach(tObject o in batch)
                {
                    DataObject d = o.getObject();
                    if (d is Command)
                    {
                        conn.InfoMessage += (object obj, SqlInfoMessageEventArgs e) =>
                        ret.Add(new Message() {
                            text =e.Message
                        });
                        Command cmd = (Command) d;
                        Debug.WriteLine("SQL: command > " + cmd.command);
                        foreach(Parameter p in cmd.parameters)
                        {
                            if (p.refId != 0)
                                p.value = batch.global[p.refId].value;
                        }
                        SqlCommand sql = sqlCommand(cmd.command, cmd.parameters);
                        sql.Connection = conn;
                        sql.Transaction = tran;

                        SqlDataReader dr = sql.ExecuteReader();
                        using(dr)
                        {
                            DataTable schema = dr.GetSchemaTable();
                            while (schema != null) {
                                Table tbl = new Table();
                                foreach(DataRow r in schema.Rows)
                                tbl.columns.Add(new Column((string) r["ColumnName"], (SqlDbType) (int) r["ProviderType"]));
                                while (dr.Read()) {
                                    Row row = tbl.newRow();
                                    for (int i = 0; i < dr.FieldCount; i++)
                                        row[i] = dr.GetValue(i);
                                }
                                ret.Add(tbl);
                                if (dr.NextResult()) schema = dr.GetSchemaTable();
                                else break;
                            }
                        }
                        Variables vars = new Variables();
                        vars.id = cmd.id;
                        foreach(SqlParameter p in sql.Parameters)
                        if (p.Direction != ParameterDirection.Input) {
                            string pName = p.ParameterName.TrimStart('@');
                            vars.Add(pName, p.Value);
                            Parameter cmdPar = cmd.parameters[pName];
                            if (cmdPar != null)
                                cmdPar.value = p.Value;
                        }
                        ret.Add(vars);
                    }
                }
                tran.Commit();
                return Task.FromResult(new Result(Status.Ok, ret));
            }*/
        } catch (Exception e) {
//            if (tran ?.Connection != null)tran.Rollback();
//            ЗаписатиПомилку(e);
//            return Task.FromResult(new Result(Status.error, e.Message));
        }
    }

    private class SQLCommand {
        String command;
        Parameters params;
        Listener listener;

        SQLCommand(String cmd, Listener listener) {
            command = cmd;
            this.listener = listener;
        }

        SQLCommand(String cmd, Parameters params, Listener listener) {
            this(cmd, listener);
//            try {
//                this.command = new String(cmd.getBytes("windows-1251"), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            this.params = params;
        }

        SQLCommand(String cmd, Parameters params) {
            this.command = cmd;
            this.params = params;
        }


        private CallableStatement getStatement() throws SQLException {
            StringBuilder sql = new StringBuilder("?=call " + command);
            boolean comma = false;
            if (params != null) {
                for (Parameter p : params) {
                    if (p.direction != Direction.none)
                        sql.append(comma ? "," : " ").append(p.getParameterName()).append("=?");
                    comma = true;
                }
            }
            CallableStatement stmt = connection.prepareCall(sql.toString());
            stmt.registerOutParameter(1, Types.INTEGER);
            if (params != null) {
                for (Parameter p : params) {
                    if (p.direction == Direction.input || p.direction == Direction.inputOutput) {
                        String s = p.getName();
                        if (s == null) Log.e("Data", "Name of field is NULL");
                        Object value = p.getValue();
                        if (value == null) stmt.setNull(p.getParameterName(), Types.NULL);
                        else
                            switch (p.type) {
                                case Int:
                                    stmt.setInt(p.getParameterName(), (Integer) value);
                                    break;
                                case DateTime:
                                case Date:
                                    stmt.setDate(p.getParameterName(), new java.sql.Date(((Date) value).getTime()));
                                    break;
                                case Float:
                                    stmt.setDouble(p.getParameterName(), (Double) value);
                                    break;
                                case VarChar:
                                    stmt.setString(p.getParameterName(), (String) value);
                                    break;
                                default:
                                    stmt.setObject(p.getParameterName(), value);
                                    break;
                            }
                    }
                }
            }
            if (params != null) {
                for (Parameter p : params) {
                    if (p.direction == Direction.inputOutput || p.direction == Direction.output)
                        stmt.registerOutParameter(p.getParameterName(), SQLType(p));
                }
            }
            return stmt;
        }
    }

    private int SQLType(Parameter p) {
        switch (p.type) {
            case Int:
                return Types.INTEGER;
            case Date:
                return Types.DATE;
            case Float:
                return Types.DOUBLE;
            case VarChar:
                return Types.VARCHAR;
            default:
                return Types.NULL;
        }
    }

    private Table getTable(JtdsResultSet rs) {
        try {
            Table tbl;
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            tbl = new Table(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                String name = metaData.getColumnName(i);
                int type = metaData.getColumnType(i);
                tbl.getColumns().add(new Column(name, type));
            }
            while (rs.next()) {
                Row row = tbl.newRow();

                for (int i = 1; i <= columnCount; i++) {
                    Object o = rs.getObject(i);
                    row.setValue(i - 1, o);
                }
            }
            return tbl;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private class GetTableAsync extends AsyncTask<SQLCommand, Object, Result> {
        Table tbl = null;
        //Result result;
        SQLCommand cmd;

        @Override
        protected Result doInBackground(SQLCommand... sqlCommands) {
            cmd = sqlCommands[0];
            try {
                CallableStatement stmt = cmd.getStatement();
                JtdsResultSet rs = (JtdsResultSet) stmt.executeQuery();
                tbl = getTable(rs);
            } catch (SQLException e) {
                e.printStackTrace();
                return new Result(ua.com.info.data.Status.Error, e.getMessage());
            }
            return new Result(ua.com.info.data.Status.Ok, tbl);

        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (cmd.listener != null)
                cmd.listener.onExecuted(result);
        }
    }

    private Variables getVariables(SQLCommand cmd, CallableStatement stmt) {
        Variables vars = new Variables();
        try {
//            stmt.getMoreResults();
            for (Parameter p : cmd.params) {
                String name = p.getName().toLowerCase();
                if (p.direction != Direction.input) {
                    Object o;
                    switch (p.type) {
                        case Bit:
                            o = stmt.getBoolean(name);
                            break;
                        case Int:
                            o = stmt.getInt(name);
                            break;
                        case VarChar:
                            o = stmt.getString(name);
                            break;
                        case DateTime:
                        case Date:
                            o = stmt.getDate(name);
                            break;
                        case Float:
                            o = stmt.getFloat(name);
                            break;
                        default:
                            o = stmt.getObject(name);
                            break;
                    }
                    vars.getValues().add(new Variable(name, o));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vars;
    }


    private class GetDataAsync extends AsyncTask<SQLCommand, Object, Result> {
        Table tbl = null;
        //Result result;
        SQLCommand cmd;

        @Override
        protected Result doInBackground(SQLCommand... sqlCommands) {
            cmd = sqlCommands[0];
            JtdsResultSet rs = null;
            try {
                CallableStatement stmt = null;
                try {
                    stmt = cmd.getStatement();
                    rs = (JtdsResultSet) stmt.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return error(e.getMessage());
                }
                //  return new Result(ua.com.info.data.Status.error, e.getMessage());
                //  ResultSetMetaData metaData = rs.getMetaData();
                // int columnCount = metaData.getColumnCount();
//                tbl = new Table(columnCount);
//                for (int i = 1; i <= columnCount; i++) {
//                    String name = metaData.getColumnName(i);
//                    int type = metaData.getColumnType(i);
//                    tbl.columns.add(new Column(name, type));
//                }
//                while (rs.next()) {
//                    Row row = tbl.newRow();
//
//                    for (int i = 1; i <= columnCount; i++) {
//                        Object o = rs.getObject(i);
//                        row.setValue(i - 1, o);
//                   }
                //              }

                Data data = new Data();
                if (rs != null)
                    data.add(getTable(rs));
                data.add(getVariables(cmd, stmt));
                return new Result(ua.com.info.data.Status.Ok, data);
            } catch (Exception e) {
                return new Result(ua.com.info.data.Status.Error, e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (cmd.listener != null)
                cmd.listener.onExecuted(result);
        }
    }

    private class ExecuteAsync extends AsyncTask<SQLCommand, Object, Result> {
        SQLCommand cmd;
        CallableStatement stmt;

        @Override
        protected Result doInBackground(SQLCommand... sqlCommands) {
            cmd = sqlCommands[0];
            int ret;
            try {
                stmt = cmd.getStatement();
                stmt.execute();
                Variables vars = getVariables(cmd, stmt);

//                if (parameters != null) foreach (Variable var in vars.values)
//                parameters.trySetValue(var.name, var.value);

                ret = stmt.getInt(1);
                vars.getValues().add(new Variable("RETURN_VALUE", ret));
                return new Result(ua.com.info.data.Status.Ok, vars);
            } catch (SQLException e) {
                e.printStackTrace();
                return new Result(ua.com.info.data.Status.Error, e.toString());
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result.isOk()) {
                Parameters params = cmd.params;
                if (cmd.params != null) {
                    try {
                        for (Parameter p : cmd.params) {
                            if (p.direction == Direction.inputOutput || p.direction == Direction.output)
                                switch (p.type) {
                                    case Int:
                                        int i = stmt.getInt(p.getParameterName());
                                        p.setValue(i);
                                        break;
                                    case Float:
                                        p.setValue(stmt.getDouble(p.getParameterName()));
                                        break;
                                    case VarChar:
                                        p.setValue(stmt.getString(p.getParameterName()));
                                        break;
                                }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            cmd.listener.onExecuted(result);
        }
    }

    private class QueryAsync extends AsyncTask<String, Object, Result> {
        String query;

        @Override
        protected Result doInBackground(String... s) {
            query = s[0];
            try {
                Statement stmt = connection.createStatement();
                stmt.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return new Result(ua.com.info.data.Status.Error, e.toString());
            }
            return new Result(ua.com.info.data.Status.Ok, null);
        }
    }
}

