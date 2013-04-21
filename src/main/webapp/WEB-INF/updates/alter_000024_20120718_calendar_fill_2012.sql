if exists (select *
            from  sysobjects
            where  id = object_id('fill_calendar')
            and    type = 'P') begin
DROP PROCEDURE fill_calendar
end
go

CREATE PROCEDURE DBA.fill_calendar( y integer )
begin
  declare i integer;
  declare d date;
  declare days integer;
  set days = 366;

  if (year(now()) % 4 = 0) then
	set days = 367
  end if;
  set d = "date"(convert(varchar(4),y)+'-01-01');
  set i = 0;
  while i < days loop
    //    select dateadd(day,i,d);
    insert into calendar( id_date,holiday ) values( dateadd(day,i,d),0 ) ;
    set i = i+1;
    if year(dateadd(day,i,d)) <> y then
      set i = i+100
    end if
  end loop;
  update dba.calendar set holiday = 1 where DOW(id_date) in( 1,7 ) and Year(id_date) = y;
  return(i)
end
go

exec fill_calendar(2012)
go