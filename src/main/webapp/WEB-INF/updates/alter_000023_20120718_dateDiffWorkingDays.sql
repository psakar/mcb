/*
SELECT dba.dateDiffWorkingDays(getdate(), dateadd(week, 1, getdate())),  dba.dateDiffWorkingDays(dateadd(week, 1, getdate()), getdate())
 */
if exists (select *
            from  sysobjects
            where  id = object_id('dateDiffWorkingDays')
            and    type = 'P') begin
DROP PROCEDURE dateDiffWorkingDays
end
go


CREATE FUNCTION DBA.dateDiffWorkingDays(in @dateFrom t_date, in @dateTo t_date)
returns integer
begin
  declare @cnt integer;
  if @dateFrom > @dateTo then
    set @cnt = isnull( (select count()*-1 from dba.calendar where id_date >= @dateTo and id_date < @dateFrom and holiday = 0), null)
  else
    set @cnt = isnull( (select count() from dba.calendar where id_date > @dateFrom and id_date <= @dateTo and holiday = 0), null)
  end if;
  return @cnt
end
go
