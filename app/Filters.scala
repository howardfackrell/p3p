import customfilters.P3PFilter
import javax.inject.Inject
import play.api.http.DefaultHttpFilters
import play.api.http.EnabledFilters
import play.filters.gzip.GzipFilter

class Filters @Inject() (
                          defaultFilters: EnabledFilters,
                          gzip: GzipFilter,
                          p3pFilter: P3PFilter
                        ) extends DefaultHttpFilters(defaultFilters.filters :+ gzip :+ p3pFilter: _*)